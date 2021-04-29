package com.kiprosh.optimizeprime.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.helper.AuthenticationHelper

class SplashScreenActivity : AppCompatActivity() {
    lateinit var redirectIntent: Intent
    lateinit var authenticationHelper: AuthenticationHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        authenticationHelper = AuthenticationHelper(this)
        Handler().postDelayed({
            val spSignIn =
                this.getSharedPreferences(authenticationHelper.keyName, Context.MODE_PRIVATE)

            redirectIntent =
                if (!spSignIn.getBoolean(authenticationHelper.keyIsFirstSignIn, true)) {
                    Intent(
                        this@SplashScreenActivity,
                        SignInActivity::class.java
                    )
                } else {
                    Intent(
                        this@SplashScreenActivity,
                        IntroActivity::class.java
                    )
                }
            startActivity(redirectIntent)
            finish()
        }, SPLASH_SCREEN_TIME_OUT.toLong())
    }

    companion object {
        private const val SPLASH_SCREEN_TIME_OUT = 2000
    }
}