package com.kiprosh.optimizeprime.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.kiprosh.optimizeprime.R

class IntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(
            AppIntroFragment.newInstance(
                description = "Never miss to wish your colleagues\non their special day!",
                backgroundColor = ContextCompat.getColor(applicationContext, R.color.blue),
                imageDrawable = R.drawable.ic_greeting
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                description = "Keep track of the upcoming events\nin your workplace",
                backgroundColor = ContextCompat.getColor(applicationContext, R.color.baby_pink),
                imageDrawable = R.drawable.ic_track
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                description = "Keep track of the upcoming events\nin your workplace",
                backgroundColor = ContextCompat.getColor(applicationContext, R.color.yellow),
                imageDrawable = R.drawable.ic_track
            )
        )

        setTransformer(AppIntroPageTransformerType.Parallax())
        isColorTransitionsEnabled = true
        setImmersiveMode()
    }

    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        openLoginActivity()
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        openLoginActivity()
    }

    private fun openLoginActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}