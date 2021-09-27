package com.sign.deftpdf.ui.registration

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityCreateAccountBinding
import com.sign.deftpdf.model.login.AuthModel
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.ui.login.LoginWebViewActivity
import com.sign.deftpdf.ui.main.GetUserPresenter
import com.sign.deftpdf.ui.main.GetUserView
import com.sign.deftpdf.util.LocalSharedUtil

class CreateAccountActivity : BaseActivity(R.layout.activity_create_account), CreateAccountView,
        GetUserView {

    private val binding by viewBinding(ActivityCreateAccountBinding::bind)
    private val presenter: CreateAccountPresenter = CreateAccountPresenter(this)
    private val presenterUser: GetUserPresenter = GetUserPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.attachView(this)
        presenterUser.attachView(this)
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
            signUpGoogle.setOnClickListener { startActivity(Intent(this@CreateAccountActivity, LoginWebViewActivity::class.java)) }
            createAccount.setOnClickListener { registration() }
            haveAccount.setOnClickListener { openLogin() }
            passShower.setOnClickListener { visibilityPassword() }
            passRepeatShower.setOnClickListener { visibilityPasswordRepeat() }
        }
    }

    private fun checkData(): Boolean {
        with(binding) {
            if (nameEdittext.text.isNullOrEmpty())
                return false
            if (!Patterns.EMAIL_ADDRESS.matcher(emailEdittext.text).matches())
                return false
            if (passEdittext.text.isNullOrEmpty())
                return false
            if (passEdittextRepeat.text.isNullOrEmpty())
                return false
            if (passEdittextRepeat.text.toString() != passEdittext.text.toString())
                return false
        }
        return true
    }

    private fun visibilityPassword() {
        with(binding) {
            if (passShower.isChecked)
                passEdittext.transformationMethod = HideReturnsTransformationMethod()
            else
                passEdittext.transformationMethod = PasswordTransformationMethod()
        }
    }

    private fun visibilityPasswordRepeat() {
        with(binding) {
            if (passRepeatShower.isChecked)
                passEdittextRepeat.transformationMethod = HideReturnsTransformationMethod()
            else
                passEdittextRepeat.transformationMethod = PasswordTransformationMethod()
        }
    }

    private fun registration() {
        if (checkData())
            presenter.sendResponse(
                binding.emailEdittext.text.toString(),
                binding.passEdittext.text.toString(),
                binding.nameEdittext.text.toString()
            )
        else
            showError("Pass or email incorrect")
    }

    override fun createAccountSuccess(data: AuthModel) {
        data.data?.token?.let {
            LocalSharedUtil().setTokenParameter(it, this)
            presenterUser.sendResponse(it)
        }
    }

    override fun getUserSuccess(data: UserModel) {
        DeftApp.user = data.data!!
        openMain()
    }
}