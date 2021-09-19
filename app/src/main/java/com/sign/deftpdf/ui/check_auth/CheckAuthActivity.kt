package com.sign.deftpdf.ui.check_auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.databinding.ActivityCheckAuthBinding
import com.sign.deftpdf.ui.login.LoginActivity
import com.sign.deftpdf.ui.registration.CreateAccountActivity

class CheckAuthActivity : AppCompatActivity(R.layout.activity_check_auth) {

    private val binding by viewBinding(ActivityCheckAuthBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListeners()
    }

    private fun initListeners(){
        with(binding){
            login.setOnClickListener{
                openLogin()
            }
            createAccount.setOnClickListener{
                openCreateAccount()
            }
        }
    }

    private fun openLogin(){
        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun openCreateAccount(){
        startActivity(Intent(this, CreateAccountActivity::class.java))
    }

}