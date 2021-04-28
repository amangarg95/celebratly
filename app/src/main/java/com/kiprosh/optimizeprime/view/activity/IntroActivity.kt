package com.kiprosh.optimizeprime.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.helper.AuthenticationHelper

class IntroActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(
            AppIntroFragment.newInstance(
                description = "Never miss to wish your colleagues\non their special day",
                descriptionColor = resources.getColor(R.color.bottom_bar_pistachio_green),
                backgroundColor = ContextCompat.getColor(
                    applicationContext,
                    R.color.white_with_blue
                ),
                imageDrawable = R.drawable.ic_1
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                description = "Keep track of the upcoming events\nin your workplace",
                descriptionColor = resources.getColor(R.color.bottom_bar_blue),
                backgroundColor = ContextCompat.getColor(applicationContext, R.color.white),
                imageDrawable = R.drawable.ic_3
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                description = "Make sure you never celebrate\nyour special day, alone",
                descriptionColor = resources.getColor(R.color.text_danger),
                backgroundColor = ContextCompat.getColor(applicationContext, R.color.white),
                imageDrawable = R.drawable.ic_2
            )
        )

        setTransformer(AppIntroPageTransformerType.Parallax())
        isColorTransitionsEnabled = true
        setIndicatorColor(
            selectedIndicatorColor = resources.getColor(R.color.bottom_bar_pistachio_green),
            unselectedIndicatorColor = resources.getColor(R.color.yellow)
        )

        setImmersiveMode()
    }

    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        AuthenticationHelper(this).saveSignInStatus()
        openSignInActivity()
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        AuthenticationHelper(this).saveSignInStatus()
        openSignInActivity()
    }

    private fun openSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}