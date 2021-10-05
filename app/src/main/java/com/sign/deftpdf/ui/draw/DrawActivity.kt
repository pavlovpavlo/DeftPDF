package com.sign.deftpdf.ui.draw

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.gson.Gson
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.custom_views.signature.SignatureUtils
import com.sign.deftpdf.databinding.ActivityDrawBinding
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.ui.main.GetUserView
import com.sign.deftpdf.util.Util
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.util.*


class DrawActivity : BaseActivity(R.layout.activity_draw) {

    private val binding by viewBinding(ActivityDrawBinding::bind)
    private var selectWidth = 3f
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
            //loadPdfToServer(contentUri)
            if(contentUri != null) {
                var bitmap: Bitmap? = null
                try {
                    val input = this.contentResolver.openInputStream(contentUri!!)
                    bitmap = BitmapFactory.decodeStream(input)
                    input!!.close()
                    if (bitmap != null) {
                        if (isFreeStyle) {
                            listener.saveImageBitmap(bitmap)
                            finish()
                        } else {
                            saveBitmapImage(bitmap)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    companion object {
        lateinit var listener: OnDrawSaveListener
        var isSign = true
        var isFreeStyle = false
        var isAccountScreen = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isFreeStyle = false
        isAccountScreen = false
        isSign = true
    }

    override fun startLoader() {
        findViewById<View>(R.id.progress_bar).visibility = View.VISIBLE
    }

    override fun stopLoader() {
        findViewById<View>(R.id.progress_bar).visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            actionSmall.setOnClickListener {
                selectWidth = 3f
                binding.inkSignatureOverlayView.strokeWidth = selectWidth
                actionSmall.setImageResource(R.drawable.ic_draw_small)
                actionBig.setImageResource(R.drawable.ic_draw_big_off)
            }
            actionBig.setOnClickListener {
                selectWidth = 6f
                binding.inkSignatureOverlayView.strokeWidth = selectWidth
                actionSmall.setImageResource(R.drawable.ic_draw_small_off)
                actionBig.setImageResource(R.drawable.ic_draw_big)
            }
            actionOpenFile.setOnClickListener {
                pickImage.launch(Util.MIMETYPE_IMAGES)
            }
            actionClear.setOnClickListener {
                clearSignature()
                enableClear(false)
                enableSave(false)
            }
            save.setOnClickListener {
                //inkSignatureOverlayView.drawTransparent()
                backImage.setImageBitmap(inkSignatureOverlayView.image)

                val drawable = backImage.drawable
                val bitmap: Bitmap = (drawable as BitmapDrawable).bitmap

                val viewHolder: SignatureUtils.ViewHolder =
                    SignatureUtils.ViewHolder()
                val arrayList: ArrayList<ArrayList<Float>> = inkSignatureOverlayView.mInkList
                val rectF: RectF = inkSignatureOverlayView.boundingBox
                viewHolder.inkList = arrayList
                viewHolder.boundingBox = rectF
                viewHolder.inkColor = inkSignatureOverlayView.mStrokeColor
                viewHolder.strokeWidth = inkSignatureOverlayView.strokeWidth

                if (isFreeStyle) {
                    listener.saveImageViewHolder(viewHolder)
                    finish()
                } else {
                    saveViewHolderImage(bitmap, viewHolder)
                }
            }
            close.setOnClickListener { finish() }
        }
    }

    /*private fun saveImage(finalBitmap: Bitmap, image_name: String) {
        val myDir = File(getExternalFilesDir(null)?.absolutePath, "pdfsdcard_location")
        if (!myDir.exists()) {
            myDir.mkdir()
        }
        val fname = "Image-$image_name.png"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()

        try {
            val out = FileOutputStream(file)
            finalBitmap.setHasAlpha(true);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }*/

    private fun saveBitmapImage(bitmap: Bitmap) {
        val body = getBodyFromBitmap(bitmap)

        if (isSign) {
            if (DeftApp.user.sign?.url.isNullOrEmpty()) {
                sendRequestSave(DeftApp.user.apiToken.toString(), body,
                    "sign", null, object : GetUserView {
                        override fun getUserSuccess(data: UserModel) {
                            DeftApp.user.sign = data.data?.sign
                            listener.saveImageBitmap(bitmap)

                            finish()
                        }
                    })
            } else {
                sendRequestUpdate(DeftApp.user.apiToken.toString(),
                    DeftApp.user.initials!!.id.toString(), body,
                    "sign", null, object : GetUserView {
                        override fun getUserSuccess(data: UserModel) {
                            DeftApp.user.sign = data.data?.sign
                            listener.saveImageBitmap(bitmap)
                            finish()
                        }
                    })
            }
        } else {
            if (DeftApp.user.initials?.url.isNullOrEmpty()) {
                sendRequestSave(DeftApp.user.apiToken.toString(), body,
                    "initials", null, object : GetUserView {
                        override fun getUserSuccess(data: UserModel) {
                            DeftApp.user.initials = data.data?.initials
                            listener.saveImageBitmap(bitmap)

                            finish()
                        }
                    })
            } else {
                sendRequestUpdate(DeftApp.user.apiToken.toString(),
                    DeftApp.user.initials!!.id.toString(), body,
                    "initials", null, object : GetUserView {
                        override fun getUserSuccess(data: UserModel) {
                            DeftApp.user.initials = data.data?.initials
                            listener.saveImageBitmap(bitmap)

                            finish()
                        }
                    })
            }
        }
    }

    private fun saveViewHolderImage(bitmap: Bitmap, viewHolder: SignatureUtils.ViewHolder) {
        val body = getBodyFromBitmap(bitmap)

        if (isSign) {
            if (DeftApp.user.sign?.url.isNullOrEmpty()) {
                sendRequestSave(DeftApp.user.apiToken.toString(), body,
                    "sign", Gson().toJson(viewHolder), object : GetUserView {
                        override fun getUserSuccess(data: UserModel) {
                            DeftApp.user.sign = data.data?.sign
                            if (isAccountScreen)
                                listener.saveImageBitmap(bitmap)
                            else
                                listener.saveImageViewHolder(viewHolder)

                            finish()
                        }
                    })
            } else {
                sendRequestUpdate(DeftApp.user.apiToken.toString(),
                    DeftApp.user.sign!!.id.toString(), body,
                    "sign", Gson().toJson(viewHolder), object : GetUserView {
                        override fun getUserSuccess(data: UserModel) {
                            DeftApp.user.sign = data.data?.sign
                            if (isAccountScreen)
                                listener.saveImageBitmap(bitmap)
                            else
                                listener.saveImageViewHolder(viewHolder)

                            finish()
                        }
                    })
            }
        } else {
            if (DeftApp.user.initials?.url.isNullOrEmpty()) {
                sendRequestSave(DeftApp.user.apiToken.toString(), body,
                    "initials", Gson().toJson(viewHolder), object : GetUserView {
                        override fun getUserSuccess(data: UserModel) {
                            DeftApp.user.initials = data.data?.initials
                            if (isAccountScreen)
                                listener.saveImageBitmap(bitmap)
                            else
                                listener.saveImageViewHolder(viewHolder)

                            finish()
                        }
                    })
            } else {
                sendRequestUpdate(DeftApp.user.apiToken.toString(),
                    DeftApp.user.initials!!.id.toString(), body,
                    "initials", Gson().toJson(viewHolder), object : GetUserView {
                        override fun getUserSuccess(data: UserModel) {
                            DeftApp.user.initials = data.data?.initials
                            if (isAccountScreen)
                                listener.saveImageBitmap(bitmap)
                            else
                                listener.saveImageViewHolder(viewHolder)
                            finish()
                        }
                    })
            }
        }
    }

    private fun getBodyFromBitmap(bitmap: Bitmap): MultipartBody.Part {
        val leftImageFile = convertBitmapToFile("image", bitmap)
        val reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), leftImageFile)
        return MultipartBody.Part.createFormData("image", leftImageFile.name, reqFile)
    }

    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.setHasAlpha(true)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos)

        //saveImage(bitmap, "test")
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    private fun sendRequestUpdate(
        token: String, documentId: String,
        image: MultipartBody.Part?, type: String,
        signSignature: String?, listener: GetUserView
    ) {
        val presenter = SignatureUpdatePresenter(this)
        presenter.attachView(listener)
        presenter.sendResponse(token, documentId, image, type, signSignature)
    }

    private fun sendRequestSave(
        token: String, image: MultipartBody.Part?, type: String,
        signSignature: String?, listener: GetUserView
    ) {
        val presenter = SignatureUpdatePresenter(this)
        presenter.attachView(listener)
        presenter.sendResponseStore(token, image, type, signSignature)
    }

    fun onRadioButtonClicked(view: View) {
        when (view.id) {
            R.id.radioBlack -> {
                binding.inkSignatureOverlayView.setStrokeColor(
                    ContextCompat.getColor(
                        this@DrawActivity,
                        R.color.draw_black
                    )
                )

            }
            R.id.radioRed -> {
                binding.inkSignatureOverlayView.setStrokeColor(
                    ContextCompat.getColor(
                        this@DrawActivity,
                        R.color.draw_red
                    )
                )
            }
            R.id.radioFiol -> binding.inkSignatureOverlayView.setStrokeColor(
                ContextCompat.getColor(
                    this@DrawActivity,
                    R.color.main
                )
            )
        }
        binding.inkSignatureOverlayView.strokeWidth = 1f
        binding.inkSignatureOverlayView.strokeWidth = selectWidth
    }

    private fun clearSignature() {
        binding.inkSignatureOverlayView.clear()
        binding.inkSignatureOverlayView.setEditable(true)
    }

    fun enableClear(value: Boolean) {
        binding.actionClear.isEnabled = value
        if (value) {
            binding.actionClear.alpha = 1.0f
        } else {
            binding.actionClear.alpha = 0.5f
        }
    }

    fun enableSave(value: Boolean) {
        if (value) {
            binding.save.alpha = 1f
        } else {
            binding.save.alpha = 0.5f
        }
        binding.save.isEnabled = value
    }

}