package com.sign.deftpdf.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.ui.check_auth.CheckAuthActivity
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.ui.main.GetUserPresenter
import com.sign.deftpdf.ui.main.GetUserView
import com.sign.deftpdf.ui.main.MainActivity
import com.sign.deftpdf.util.LocalSharedUtil

class SplashActivity : BaseActivity(R.layout.activity_splash), GetUserView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token: String = LocalSharedUtil().getTokenParameter(this)

        if (token == "")
            Handler(Looper.getMainLooper()).postDelayed({ openScreenCheck() }, 2000L)
        else {
            val presenter = GetUserPresenter(this)
            presenter.attachView(this)
            presenter.sendResponse(token)
        }
    }

    private fun openScreenCheck() {
        startActivity(Intent(this, CheckAuthActivity::class.java))
        finish()
    }

    private fun openScreenMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun getUserSuccess(data: UserModel) {
        DeftApp.user = data.data!!
        openScreenMain()
    }

    override fun showError(message: String) {
        openScreenCheck()
    }

    override fun startLoader() {
    }

    override fun stopLoader() {
    }
}