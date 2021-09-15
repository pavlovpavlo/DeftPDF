package com.sign.deftpdf.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns.EMAIL_ADDRESS
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.ui.forgot.ForgotPassActivity
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityLoginBinding
import com.sign.deftpdf.model.login.AuthModel
import com.sign.deftpdf.ui.MainActivity
import com.sign.deftpdf.ui.registration.CreateAccountActivity

class LoginActivity : BaseActivity(R.layout.activity_login), LoginView {

    private val binding by viewBinding(ActivityLoginBinding::bind)
    private val presenter: LoginPresenter = LoginPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.attachView(this)
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            backBtn.setOnClickListener { finish() }
            login.setOnClickListener { login() }
            //loginGoogle.setOnClickListener { login() }
            forgotPass.setOnClickListener { openForgotPass() }
            createAccount.setOnClickListener { openCreateAccount() }
            passVisible.setOnClickListener { visibilityPassword() }
        }
    }

    private fun checkData(): Boolean {
        with(binding) {
            if (passEdittext.text.isNullOrEmpty())
                return false
            if (!EMAIL_ADDRESS.matcher(emailEdittext.text).matches())
                return false
        }
        return true
    }

    private fun visibilityPassword() {
        with(binding) {
            if (passVisible.isChecked)
                passEdittext.transformationMethod = HideReturnsTransformationMethod()
            else
                passEdittext.transformationMethod = PasswordTransformationMethod()

        }
    }

    private fun login() {
        if (checkData())
            presenter.sendResponse(
                binding.emailEdittext.text.toString(),
                binding.passEdittext.text.toString()
            )
        else
            showError("Pass or email incorrect")
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun openCreateAccount() {
        startActivity(Intent(this, CreateAccountActivity::class.java))
    }

    private fun openForgotPass() {
        startActivity(Intent(this, ForgotPassActivity::class.java))
    }

    override fun loginSuccess(data: AuthModel) {
        openMain()
    }
}