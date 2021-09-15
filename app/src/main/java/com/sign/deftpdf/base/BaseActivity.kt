package com.sign.deftpdf.base

import androidx.appcompat.app.AppCompatActivity
import com.sign.deftpdf.util.NoInternetConnectionUtil

open class BaseActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId), BasicView {
    override fun showError(message: String) {

    }

    override fun showInternetError(onRefreshResponse: () -> Unit) {
        NoInternetConnectionUtil().showNoInternetDialog(this, onRefreshResponse)
        stopLoader()
    }

    override fun startLoader() {

    }

    override fun stopLoader() {

    }
}