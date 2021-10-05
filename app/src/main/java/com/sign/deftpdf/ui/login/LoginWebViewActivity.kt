package com.sign.deftpdf.ui.login

import android.os.Bundle
import android.util.JsonReader
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityLoginWebViewBinding
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.ui.main.GetUserPresenter
import com.sign.deftpdf.ui.main.GetUserView
import com.sign.deftpdf.util.LocalSharedUtil
import java.io.StringReader


class LoginWebViewActivity : BaseActivity(R.layout.activity_login_web_view), GetUserView {

    private val binding by viewBinding(ActivityLoginWebViewBinding::bind)
    private val presenterUser: GetUserPresenter = GetUserPresenter(this)

    override fun startLoader() {
        findViewById<View>(R.id.progress_bar).visibility = View.VISIBLE
    }

    override fun stopLoader() {
        findViewById<View>(R.id.progress_bar).visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenterUser.attachView(this)
        startLoader()
        val webSettings: WebSettings = binding.webview.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webSettings.userAgentString = "Chrome/56.0.0.0 Mobile"
        webSettings.setSupportZoom(true)
        webSettings.defaultTextEncodingName = "utf-8"

        binding.webview.loadUrl("https://pdf.webstaginghub.com/auth-google")
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                stopLoader()
                if (binding.webview.url!!.contains("https://pdf.webstaginghub.com/account")) {
                    binding.webview.evaluateJavascript(
                        "after_login_callback()",
                        ValueCallback<String?> { s ->
                            //val reader = JsonReader(StringReader(s))
                            val token = s.replace("\"", "")

                            if (token != "null") {
                                    LocalSharedUtil().setTokenParameter(token, this@LoginWebViewActivity)
                                    presenterUser.sendResponse(token)
                                }
                            else
                                showError("Auth error, token not correct")
                        })
                }
            }
        }
    }

    override fun getUserSuccess(data: UserModel) {
        DeftApp.user = data.data!!
        openMain()
    }
}