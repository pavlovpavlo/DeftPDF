package com.sign.deftpdf.ui.request_signature

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.base.BaseModelView
import com.sign.deftpdf.databinding.ActivityRequestSignatureBinding
import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.model.documents.DocumentsData
import com.sign.deftpdf.model.sign_link.SignLinkModel

class RequestSignatureActivity : BaseActivity(R.layout.activity_request_signature) {

    internal val binding by viewBinding(ActivityRequestSignatureBinding::bind)
    lateinit var data: DocumentsData
    private lateinit var lastActiveTabs: TextView
    private var isCodeGet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = intent.getSerializableExtra("document") as DocumentsData
        initListeners()
    }

    override fun startLoader() {
        findViewById<View>(R.id.progress_bar).visibility = View.VISIBLE
    }

    override fun stopLoader() {
        findViewById<View>(R.id.progress_bar).visibility = View.GONE
    }

    private fun initListeners() {
        with(binding) {
            lastActiveTabs = emailTab
            emailTab.setOnClickListener {
                setActiveTabs(it)
                groupLink.visibility = View.GONE
                groupEmail.visibility = View.VISIBLE
                sendRequest.text = getText(R.string.send_request)
                sendRequest.setOnClickListener {
                    defaultClickListener()
                }
            }
            linkTab.setOnClickListener {
                setActiveTabs(it)
                groupEmail.visibility = View.GONE
                groupLink.visibility = View.VISIBLE

                if(isCodeGet) {
                    sendRequest.text = getText(R.string.copy_link)
                    sendRequest.setOnClickListener {
                        copyClickListener()
                    }
                }else{
                    sendRequest.text = getText(R.string.get_link)
                    sendRequest.setOnClickListener {
                        defaultClickListener()
                    }
                }
            }
            sendRequest.setOnClickListener {
                defaultClickListener()
            }
            backBtn.setOnClickListener { finish() }

            closeKeyboard.setOnClickListener(View.OnClickListener { v: View? ->
                hideKeyboard(
                    this@RequestSignatureActivity
                )
            })

        }
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun defaultClickListener(){
        if (checkData()) {
            if (lastActiveTabs.id == R.id.email_tab)
                sendEmail()
            else
                getLink()
        }
    }

    private fun copyClickListener(){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", binding.linkEdittext.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(
            this@RequestSignatureActivity,
            "Copied to clipboard",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkData(): Boolean {
        with(binding) {
            if (nameEdittext.text.isNullOrEmpty())
                return false
            if (!Patterns.EMAIL_ADDRESS.matcher(emailEdittext.text).matches())
                return false
        }
        return true
    }

    private fun setActiveTabs(view: View) {
        lastActiveTabs.setBackgroundResource(R.drawable.ic_document_filter_off_bg)
        lastActiveTabs.setTextColor(getColor(R.color.main))
        lastActiveTabs = view as TextView
        lastActiveTabs.setBackgroundResource(R.drawable.ic_document_filter_bg)
        lastActiveTabs.setTextColor(getColor(R.color.white))
    }

    private fun sendEmail() {
        val presenter = RequestSignatureEmailPresenter(this)
        presenter.attachView(object : BaseModelView {
            override fun requestSuccess(data: BaseModel) {
                Toast.makeText(
                    this@RequestSignatureActivity,
                    "Data has been successfully sent by email",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        presenter.sendResponse(
            DeftApp.user.apiToken.toString(),
            data.id.toString(),
            binding.emailEdittext.text.toString(),
            binding.nameEdittext.text.toString(),
            binding.noteEdittext.text.toString()
        )
    }

    private fun getLink() {
        val presenter = RequestSignatureLinkPresenter(this)
        presenter.attachView(object : RequestLinkView {
            override fun requestSuccess(data: SignLinkModel) {
                data.data?.url?.let {
                    binding.copyLink.visibility = View.INVISIBLE
                    binding.linkEdittext.text = it
                    binding.linkEdittext.setBackgroundResource(R.drawable.ic_edittext_bg_link)
                    isCodeGet = true
                    binding.sendRequest.text = getText(R.string.copy_link)
                    binding.sendRequest.setOnClickListener {
                        copyClickListener()
                    }
                }
            }

        })
        presenter.sendResponse(
            DeftApp.user.apiToken.toString(),
            data.id.toString(),
            binding.emailEdittext.text.toString(),
            binding.nameEdittext.text.toString()
        )
    }
}