package com.sign.deftpdf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.databinding.ActivityCreateAccountBinding
import com.sign.deftpdf.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val binding by viewBinding(ActivityLoginBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            backBtn.setOnClickListener { finish() }
            login.setOnClickListener { openMain() }
            loginGoogle.setOnClickListener { openMain() }
            forgotPass.setOnClickListener { openForgotPass() }
            createAccount.setOnClickListener { openCreateAccount() }
        }
    }

    private fun openMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun openCreateAccount(){
        startActivity(Intent(this, CreateAccountActivity::class.java))
    }

    private fun openForgotPass(){
        startActivity(Intent(this, ForgotPassActivity::class.java))
    }
}