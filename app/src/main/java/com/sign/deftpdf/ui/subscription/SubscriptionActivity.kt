package com.sign.deftpdf.ui.subscription

import android.os.Bundle
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivitySubscriptionBinding
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.ui.main.GetUserPresenter
import com.sign.deftpdf.ui.main.GetUserView
import com.sign.deftpdf.util.LocalSharedUtil

class SubscriptionActivity : BaseActivity(R.layout.activity_subscription), GetUserView {

    private val binding by viewBinding(ActivitySubscriptionBinding::bind)
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

        binding.webview.loadUrl("https://pdf.webstaginghub.com/membership-plans?api_token=${DeftApp.user.apiToken.toString()}")
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                stopLoader()
                if (binding.webview.url!!.contains("https://pdf.webstaginghub.com/account/subscription-app-callback")
                    && !binding.webview.url!!.contains("https://pdf.webstaginghub.com/account/plans")) {
                    binding.webview.evaluateJavascript(
                        "status_subscription_callback ()") { s ->
                        //val reader = JsonReader(StringReader(s))
                        val token = s.replace("\"", "")

                        if (token == "true") {
                            presenterUser.sendResponse(DeftApp.user.apiToken.toString())
                        } else
                            showError("Pay error")
                    }
                }
            }
        }
    }

    override fun getUserSuccess(data: UserModel) {
        DeftApp.user = data.data!!
        finish()
    }
}