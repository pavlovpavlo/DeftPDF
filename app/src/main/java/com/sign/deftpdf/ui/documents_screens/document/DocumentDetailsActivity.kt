package com.sign.deftpdf.ui.documents_screens.document

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.app.ShareCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.base.BaseModelView
import com.sign.deftpdf.databinding.ActivityDocumentDetailsBinding
import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.model.document.DocumentData
import com.sign.deftpdf.model.document.DocumentModel
import com.sign.deftpdf.model.documents.DocumentsData
import com.sign.deftpdf.ui.documents_screens.DocumentsDeletePresenter
import com.sign.deftpdf.ui.documents_screens.DocumentsUpdatePresenter
import com.sign.deftpdf.ui.documents_screens.delete.DeleteDocumentDialog
import com.sign.deftpdf.ui.documents_screens.rename.RenameDocumentDialog
import com.sign.deftpdf.util.Util
import okhttp3.MultipartBody

class DocumentDetailsActivity : BaseActivity(R.layout.activity_document_details), GetDocumentView {

    internal val binding by viewBinding(ActivityDocumentDetailsBinding::bind)
    val presenter = GetDocumentPresenter(this)
    lateinit var data: DocumentData
    lateinit var inflater: LayoutInflater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        presenter.attachView(this)

        val data: DocumentsData = intent.getSerializableExtra("document") as DocumentsData

        presenter.sendResponse(DeftApp.user.apiToken.toString(), data.id.toString())
    }

    private fun initListeners(){
        with(binding){
            emailSend.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                intent.putExtra(Intent.EXTRA_EMAIL, DeftApp.user.email)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
            exportSend.setOnClickListener {
                ShareCompat.IntentBuilder.from(this@DocumentDetailsActivity)
                    .setType("text/plain")
                    .setChooserTitle("Share URL")
                    .setText(Util.DATA_URL + data.originalDocument)
                    .startChooser()
            }
            rename.setOnClickListener {
renameDocument()
            }
            delete.setOnClickListener {
deleteDocument()
            }
        }
    }

    private fun renameDocument() {
        RenameDocumentDialog.display(supportFragmentManager) { newName ->
            run {
                sendRequestRename(newName)
            }
        }
    }

    private fun sendRequestRename(newName: String) {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("", "")
            .build().part(0)
        val updatePresenter = DocumentsUpdatePresenter(this@DocumentDetailsActivity)
        updatePresenter.attachView(object : BaseModelView {
            override fun requestSuccess(data: BaseModel) {
                binding.textName.text = newName
            }
        })
        updatePresenter.sendResponse(
            DeftApp.user.apiToken!!,
            data.id.toString(),
            multipartBody, null, newName
        )
    }

    private fun deleteDocument() {
        DeleteDocumentDialog.display(
            supportFragmentManager,
            data.originalName.toString()
        ) {
            sendRequestDelete()
        }
    }

    private fun sendRequestDelete() {
        val deletePresenter = DocumentsDeletePresenter(this@DocumentDetailsActivity)
        deletePresenter.attachView(object : BaseModelView {
            override fun requestSuccess(data: BaseModel) {
                finish()
            }
        })
        deletePresenter.sendResponse(
            DeftApp.user.apiToken!!,
            data.id.toString()
        )
    }

    private fun setData(){
        with(binding){
            textName.text = data.originalName
            textType.text = data.status
            textSize.text = bytesIntoHumanReadable(data.size.toLong())
            if(data.activity.isNullOrEmpty()){
                activityTitle.visibility = View.VISIBLE
            }else {
                for (activities in data.activity!!) {
                    val view: View =
                        inflater.inflate(R.layout.item_user, null)
                    val image: TextView = view.findViewById(R.id.document_image)
                    val name: TextView = view.findViewById(R.id.document_name)
                    val date: TextView = view.findViewById(R.id.document_date)

                    image.text = activities.shareEmail?.get(0).toString()
                    name.text = activities.shareEmail
                    date.text = activities.createdAt
                }
            }
        }
    }

    private fun bytesIntoHumanReadable(bytes: Long): String? {
        val kilobyte: Long = 1024
        val megabyte = kilobyte * 1024
        val gigabyte = megabyte * 1024
        val terabyte = gigabyte * 1024
        return when {
            bytes in 0 until kilobyte -> {
                "$bytes B"
            }
            bytes in kilobyte until megabyte -> {
                (bytes / kilobyte).toString() + " KB"
            }
            bytes in megabyte until gigabyte -> {
                (bytes / megabyte).toString() + " MB"
            }
            bytes in gigabyte until terabyte -> {
                (bytes / gigabyte).toString() + " GB"
            }
            bytes >= terabyte -> {
                (bytes / terabyte).toString() + " TB"
            }
            else -> {
                "$bytes Bytes"
            }
        }
    }

    override fun requestSuccess(data: DocumentModel) {
         data.data?.let {
             this.data = it
             setData()
             initListeners()
         }
    }
}