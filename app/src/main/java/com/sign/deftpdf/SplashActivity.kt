package com.sign.deftpdf

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({ openNextScreen() }, 2000L)
    }

    private fun openNextScreen(){
        startActivity(Intent(this, CheckAuthActivity::class.java))
    }
}