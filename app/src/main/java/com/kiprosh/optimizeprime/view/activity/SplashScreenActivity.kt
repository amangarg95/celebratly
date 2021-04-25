package com.example.optimizeprimeandroidapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.optimizeprimeandroidapp.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler().postDelayed({
            val intent = Intent(
                this@SplashScreenActivity,
                MainActivity::class.java
            )
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_TIME_OUT.toLong())
    }

    companion object {
        private const val SPLASH_SCREEN_TIME_OUT = 2000
    }
}