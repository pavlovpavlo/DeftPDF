package com.sign.deftpdf

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.databinding.ActivityCreateAccountBinding

class CreateAccountActivity : AppCompatActivity(R.layout.activity_create_account) {

    private val binding by viewBinding(ActivityCreateAccountBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            backBtn.setOnClickListener { finish() }
            signUpGoogle.setOnClickListener { openMain() }
            createAccount.setOnClickListener { openMain() }
            haveAccount.setOnClickListener { openLogin() }
        }
    }

    private fun openMain(){
        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun openLogin(){
        startActivity(Intent(this, LoginActivity::class.java))
    }
}