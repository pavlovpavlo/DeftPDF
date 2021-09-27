package com.sign.deftpdf.ui.login

import android.os.Bundle
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityLoginWebViewBinding


class LoginWebViewActivity : BaseActivity(R.layout.activity_login_web_view) {

    private val binding by viewBinding(ActivityLoginWebViewBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                if (binding.webview.url == "https://pdf.webstaginghub.com/account"){

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.webview.evaluateJavascript("(function() { window.dispatchEvent(appResumeEvent); })();",
                ValueCallback<String?> {
                    binding.webview.loadUrl("https://pdf.webstaginghub.com/account")
                })
    }
}