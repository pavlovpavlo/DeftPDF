package com.sign.deftpdf.ui.view_document

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itextpdf.text.pdf.security.*
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.base.BaseModelView
import com.sign.deftpdf.custom_views.documents.*
import com.sign.deftpdf.custom_views.signature.SignatureUtils
import com.sign.deftpdf.databinding.ActivityDocumentViewerBinding
import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.model.documents.DocumentsData
import com.sign.deftpdf.ui.date_text_pickers.DateSelectActivity
import com.sign.deftpdf.ui.date_text_pickers.TextActivity
import com.sign.deftpdf.ui.documents_screens.DocumentsUpdatePresenter
import com.sign.deftpdf.ui.draw.DrawActivity
import com.sign.deftpdf.ui.draw.OnDrawSaveListener
import com.sign.deftpdf.ui.request_signature.RequestSignatureActivity
import com.sign.deftpdf.util.FileDownloader
import com.sign.deftpdf.util.Util
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*


class DocumentViewerActivity : BaseActivity(R.layout.activity_document_viewer), OnDrawSaveListener {

    private class DownloadFile(val activity: DocumentViewerActivity) :
        AsyncTask<String?, Void?, Void?>() {

        override fun doInBackground(vararg params: String?): Void? {
            val fileUrl = params[0]

            val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())

            if (!activity.hasPermissions(activity, activity.PERMISSIONS)) {
                ActivityCompat.requestPermissions(
                    activity,
                    activity.PERMISSIONS,
                    activity.PERMISSION_ALL
                )
            }

            val file = File(activity.getExternalFilesDir(null)?.absolutePath, "pdfsdcard_location")
            if (!file.exists()) {
                file.mkdir()
            }
            val pdfFile = File(file, "test.pdf")

            try {
                pdfFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            FileDownloader.downloadFile(fileUrl, pdfFile)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            activity.OpenPDFViewer(
                Uri.parse(
                    "file:///" + activity.getExternalFilesDir(null)?.absolutePath + "/pdfsdcard_location/test.pdf"
                )
            )
        }
    }

    private val binding by viewBinding(ActivityDocumentViewerBinding::bind)
    private val READ_REQUEST_CODE = 42
    private val SIGNATURE_Request_CODE = 43
    private val IMAGE_REQUEST_CODE = 45
    private val DIGITALID_REQUEST_CODE = 44
    var pdfData: Uri? = null
    var imageAdapter: PDSPageAdapter? = null
    private var mContext: Context? = null
    private val mRecyclerView: RecyclerView? = null
    private lateinit var data: DocumentsData
    private var mFirstTap = false
    private var mVisibleWindowHt = 0
    private var mDocument: PDSPDFDocument? = null
    private var mdigitalID: Uri? = null
    private var mmenu: Menu? = null
    var isSigned = false
    var status = "signed"
    var PERMISSION_ALL = 1
    var PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private lateinit var popup: PopupWindow

    override fun startLoader() {
        findViewById<View>(R.id.progress_bar).visibility = View.VISIBLE
    }

    override fun stopLoader() {
        findViewById<View>(R.id.progress_bar).visibility = View.GONE
    }

    fun baseSettingsDialog(layout: Int): View {
        val inflater = getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(layout, null)
        popup = PopupWindow(
            view, RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT, true
        )
        popup.animationStyle = R.style.popup_window_animation_phone
        popup.isOutsideTouchable = true
        Handler(Looper.getMainLooper()).postDelayed({
            binding.dialogBack.visibility = View.VISIBLE
        }, 150L)

        popup.setOnDismissListener {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.dialogBack.visibility = View.INVISIBLE
            }, 150L)
        }
        popup.showAtLocation(binding.bottomBar, Gravity.BOTTOM, 0, 0)

        return view;
    }

    private fun sendRequestRename(multipartBody: MultipartBody.Part, status: String) {
        val updatePresenter = DocumentsUpdatePresenter(this)
        updatePresenter.attachView(object : BaseModelView {
            override fun requestSuccess(data: BaseModel) {
                finish()
            }
        })
        updatePresenter.sendResponse(
            DeftApp.user.apiToken!!,
            data.id.toString(),
            multipartBody, status, data.originalName
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = applicationContext
        data = intent.getSerializableExtra("document") as DocumentsData
        startLoader()
        DownloadFile(this).execute(Util.DATA_URL + data.originalDocument)
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            title.text = data.originalName
            backBtn.setOnClickListener { finish() }
            signDocument.setOnClickListener { showSignDialog() }
            save.setOnClickListener { showSaveDialog() }
            freestyle.setOnClickListener {
                DrawActivity.isFreeStyle = true
                DrawActivity.listener = this@DocumentViewerActivity
                startActivity(Intent(this@DocumentViewerActivity, DrawActivity::class.java))
            }
            initials.setOnClickListener {
                if (DeftApp.user.initials?.url.isNullOrEmpty()) {
                    DrawActivity.isSign = false
                    DrawActivity.listener = this@DocumentViewerActivity
                    startActivity(Intent(this@DocumentViewerActivity, DrawActivity::class.java))
                } else {
                    if (DeftApp.user.initials?.stringSignature.isNullOrEmpty()) {
                        Glide.with(this@DocumentViewerActivity)
                            .asBitmap()
                            .load(Util.DATA_URL + DeftApp.user.initials?.url)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    saveImageBitmap(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }
                            })
                    } else {
                        saveImageViewHolder(
                            Gson().fromJson(
                                DeftApp.user.initials?.stringSignature,
                                object : TypeToken<SignatureUtils.ViewHolder?>() {}.type
                            )
                        )
                    }
                }
            }
            signature.setOnClickListener {
                if (DeftApp.user.sign?.url.isNullOrEmpty()) {
                    DrawActivity.listener = this@DocumentViewerActivity
                    startActivity(Intent(this@DocumentViewerActivity, DrawActivity::class.java))
                } else {
                    if (DeftApp.user.sign?.stringSignature.isNullOrEmpty()) {
                        Glide.with(this@DocumentViewerActivity)
                            .asBitmap()
                            .load(Util.DATA_URL + DeftApp.user.sign?.url)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    saveImageBitmap(resource)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }
                            })
                    } else {
                        saveImageViewHolder(
                            Gson().fromJson(
                                DeftApp.user.sign?.stringSignature,
                                object : TypeToken<SignatureUtils.ViewHolder?>() {}.type
                            )
                        )
                    }
                }
            }
            date.setOnClickListener {
                DateSelectActivity.listener = this@DocumentViewerActivity
                startActivity(Intent(this@DocumentViewerActivity, DateSelectActivity::class.java))
            }
            text.setOnClickListener {
                TextActivity.listener = this@DocumentViewerActivity
                startActivity(Intent(this@DocumentViewerActivity, TextActivity::class.java))
            }
        }
    }

    private fun showSignDialog() {
        val view: View = baseSettingsDialog(R.layout.dialog_sign)
        val signMe: LinearLayout = view.findViewById(R.id.sign_me)
        val signNotMe: LinearLayout = view.findViewById(R.id.sign_not_me)

        signMe.setOnClickListener {
            with(binding) {
                signDocument.visibility = View.GONE
                save.visibility = View.VISIBLE
                bottomBar.visibility = View.VISIBLE
            }
            popup.dismiss()
        }
        signNotMe.setOnClickListener {
            val intent = Intent(this, RequestSignatureActivity::class.java)
            intent.putExtra("document", data)
            startActivity(intent)
            popup.dismiss()
        }
    }

    private fun showSaveDialog() {
        val view: View = baseSettingsDialog(R.layout.dialog_save)
        val signMe: LinearLayout = view.findViewById(R.id.sign_me)
        val signNotMe: LinearLayout = view.findViewById(R.id.sign_not_me)

        signMe.setOnClickListener {
            status = "signed"
            val task = PDSSaveAsPDFAsyncTask(this, "test.pdf")
            task.execute(*arrayOfNulls<Void>(0))
            popup.dismiss()

        }
        signNotMe.setOnClickListener {
            status = "draft"
            val task = PDSSaveAsPDFAsyncTask(this, "test.pdf")
            task.execute(*arrayOfNulls<Void>(0))
            popup.dismiss()
        }
    }

    override fun saveImageBitmap(bitmap: Bitmap) {
        this.addElement(
            PDSElement.PDSElementType.PDSElementTypeImage,
            bitmap,
            resources.getDimension(R.dimen.sign_field_default_height),
            resources.getDimension(R.dimen.sign_field_default_height)
        )
    }

    override fun saveImageViewHolder(viewHolder: SignatureUtils.ViewHolder) {
        this.addElement(
            PDSElement.PDSElementType.PDSElementTypeSignature,
            Gson().toJson(viewHolder),
            resources.getDimension(R.dimen.sign_field_default_height),
            resources.getDimension(R.dimen.sign_field_default_height)
        )
    }

    fun hasPermissions(context: Context?, vararg permissions: Array<String>): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission[0]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        if (requestCode == DIGITALID_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (result != null) {
                    mdigitalID = result.data
                }
            } else {
                Toast.makeText(
                    this@DocumentViewerActivity,
                    "Digital certificate is not added with Signature",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun OpenPDFViewer(pdfData: Uri?) {
       stopLoader()
        try {
            val document = PDSPDFDocument(this, pdfData)
            document.open()
            mDocument = document
            imageAdapter = PDSPageAdapter(supportFragmentManager, document)
            binding.mViewPager.adapter = imageAdapter
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                this@DocumentViewerActivity,
                "Cannot open PDF, either PDF is corrupted or password protected",
                Toast.LENGTH_LONG
            ).show()
            finish()
        }
    }

    private fun computeVisibleWindowHtForNonFullScreenMode(): Int {
        return findViewById<View>(R.id.docviewer).height
    }


    fun isFirstTap(): Boolean {
        return mFirstTap
    }

    fun setFirstTap(z: Boolean) {
        mFirstTap = z
    }

    fun getVisibleWindowHeight(): Int {
        if (mVisibleWindowHt == 0) {
            mVisibleWindowHt = computeVisibleWindowHtForNonFullScreenMode()
        }
        return mVisibleWindowHt
    }

    fun getDocument(): PDSPDFDocument? {
        return mDocument
    }


    fun invokeMenuButton(disableButtonFlag: Boolean) {
        /*val saveItem = mmenu!!.findItem(R.id.action_save)
        saveItem.isEnabled = disableButtonFlag
        val signPDF = mmenu!!.findItem(R.id.action_sign)
        //signPDF.setEnabled(!disableButtonFlag);
        isSigned = disableButtonFlag
        if (disableButtonFlag) {
            //signPDF.getIcon().setAlpha(130);
            saveItem.icon.alpha = 255
        } else {
            //signPDF.getIcon().setAlpha(255);
            saveItem.icon.alpha = 130
        }*/
    }

    fun addElement(fASElementType: PDSElement.PDSElementType, file: String?, f: Float, f2: Float) {
        val focusedChild: View = binding.mViewPager.focusedChild
        if (focusedChild != null) {
            val fASPageViewer: PDSPageViewer =
                (focusedChild as ViewGroup).getChildAt(0) as PDSPageViewer
            if (fASPageViewer != null) {
                val visibleRect: RectF = fASPageViewer.visibleRect
                val width = visibleRect.left + visibleRect.width() / 2.0f - f / 2.0f
                val height = visibleRect.top + visibleRect.height() / 2.0f - f2 / 2.0f
                val fASElementType2: PDSElement.PDSElementType = fASElementType
                val element: PDSElement =
                    fASPageViewer.createElement(fASElementType2, file, width, height, f, f2)

            }
            invokeMenuButton(true)
        }
    }


    fun addElement(
        fASElementType: PDSElement.PDSElementType,
        bitmap: Bitmap?,
        f: Float,
        f2: Float
    ) {
        val focusedChild: View = binding.mViewPager.focusedChild
        if (focusedChild != null && bitmap != null) {
            val fASPageViewer: PDSPageViewer =
                (focusedChild as ViewGroup).getChildAt(0) as PDSPageViewer
            if (fASPageViewer != null) {
                val visibleRect: RectF = fASPageViewer.visibleRect
                val width = visibleRect.left + visibleRect.width() / 2.0f - f / 2.0f
                val height = visibleRect.top + visibleRect.height() / 2.0f - f2 / 2.0f

                val fASElementType2: PDSElement.PDSElementType = fASElementType
                val element: PDSElement =
                    fASPageViewer.createElement(fASElementType2, bitmap, width, height, f, f2)
            }
            invokeMenuButton(true)
        }
    }

    fun runPostExecution() {
        val file = File(getExternalFilesDir(null)?.absolutePath, "pdfsdcard_location")
        if (!file.exists()) {
            file.mkdir()
        }
        val pdfFile = File(file, "test.pdf")

        val requestFile = RequestBody.create(MediaType.parse("file"), pdfFile)

        val body = MultipartBody.Part.createFormData("file", pdfFile.name, requestFile)

        sendRequestRename(body, status)
    }


}