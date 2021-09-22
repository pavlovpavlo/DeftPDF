package com.sign.deftpdf.base

import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sign.deftpdf.R
import com.sign.deftpdf.model.documents.DocumentData
import com.sign.deftpdf.ui.main.MainActivity
import com.sign.deftpdf.ui.login.LoginActivity
import com.sign.deftpdf.ui.registration.CreateAccountActivity
import com.sign.deftpdf.util.NoInternetConnectionUtil

open class BaseActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId), BasicView {

    private var ERROR_TIME_SECOND = 4L

    override fun showError(message: String) {
        val countDownTimer: CountDownTimer
        findViewById<View>(R.id.error_notification).visibility = View.VISIBLE
        findViewById<View>(R.id.error_notification).setOnClickListener { v: View? ->
            findViewById<View>(
                R.id.error_notification
            ).visibility = View.GONE
        }
        (findViewById<View>(R.id.notification_text) as TextView).text = message

        countDownTimer = object : CountDownTimer(ERROR_TIME_SECOND * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                findViewById<View>(R.id.error_notification).visibility = View.GONE
            }
        }
        countDownTimer.start()
    }

    override fun showInternetError(onRefreshResponse: () -> Unit) {
        NoInternetConnectionUtil().showNoInternetDialog(this, onRefreshResponse)
        stopLoader()
    }

    override fun startLoader() {

    }

    override fun stopLoader() {

    }

    fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun openCreateAccount() {
        startActivity(Intent(this, CreateAccountActivity::class.java))
    }

    fun openDocumentShow(data: DocumentData){

    }

}