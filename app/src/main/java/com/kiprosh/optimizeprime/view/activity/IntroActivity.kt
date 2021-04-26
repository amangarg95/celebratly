package com.kiprosh.optimizeprime.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
                backgroundColor = resources.getColor(R.color.blue),
                imageDrawable = R.drawable.ic_greeting
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                description = "Keep track of the upcoming events\nin your workplace",
                backgroundColor = resources.getColor(R.color.baby_pink),
                imageDrawable = R.drawable.ic_track
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                description = "Keep track of the upcoming events\nin your workplace",
                backgroundColor = resources.getColor(R.color.yellow),
                imageDrawable = R.drawable.ic_track
            )
        )

        setTransformer(AppIntroPageTransformerType.Parallax())
        isColorTransitionsEnabled = true
        setImmersiveMode()
    }

    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}