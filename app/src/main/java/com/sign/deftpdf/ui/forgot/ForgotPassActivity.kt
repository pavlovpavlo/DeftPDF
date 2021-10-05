package com.sign.deftpdf.ui.forgot

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityForgotPassBinding
import com.sign.deftpdf.model.BaseModel

class ForgotPassActivity : BaseActivity(R.layout.activity_forgot_pass), ForgotPassView {

    private val binding by viewBinding(ActivityForgotPassBinding::bind)
    private val presenter: ForgotPassPresenter = ForgotPassPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.attachView(this)

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
            backBtn.setOnClickListener { finish() }
            resetPass.setOnClickListener { resetPass() }
            createAccount.setOnClickListener { openCreateAccount() }
            haveAccount.setOnClickListener { openLogin() }
        }
    }

    private fun resetPass() {
        if (checkData())
            presenter.sendResponse(binding.emailEdittext.text.toString())
    }

    private fun checkData(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(binding.emailEdittext.text).matches()
    }

    override fun requestSuccess(data: BaseModel) {
        Toast.makeText(this, "Password reset instructions had been sent.", Toast.LENGTH_SHORT).show()
        finish()
    }


}