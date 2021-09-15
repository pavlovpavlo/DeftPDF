package com.sign.deftpdf.ui.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.ui.login.LoginActivity
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityCreateAccountBinding
import com.sign.deftpdf.model.login.AuthModel
import com.sign.deftpdf.ui.MainActivity
import com.sign.deftpdf.ui.login.LoginPresenter

class CreateAccountActivity : BaseActivity(R.layout.activity_create_account), CreateAccountView{

    private val binding by viewBinding(ActivityCreateAccountBinding::bind)
    private val presenter: CreateAccountPresenter = CreateAccountPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            backBtn.setOnClickListener { finish() }
            //signUpGoogle.setOnClickListener { openMain() }
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

    private fun openMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun openLogin(){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun createAccountSuccess(data: AuthModel) {
       openMain()
    }
}