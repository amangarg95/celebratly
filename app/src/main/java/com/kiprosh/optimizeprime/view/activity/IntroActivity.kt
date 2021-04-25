package com.example.optimizeprimeandroidapp.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.optimizeprimeandroidapp.R
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class IntroActivity : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(
            AppIntroFragment.newInstance(
                backgroundDrawable = R.drawable.bg_intro_animals
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                backgroundDrawable = R.drawable.bg_intro_dino
            )
        )

        addSlide(
            AppIntroFragment.newInstance(
                backgroundDrawable = R.drawable.bg_intro_rabbit
            )
        )

//        addSlide(
//            AppIntroFragment.newInstance(
//                SliderPage(
//                    description = "Never miss to wish your colleagues on their special day!",
//                    backgroundColor = resources.getColor(R.color.intro_slide_2),
//                    imageDrawable = R.drawable.ic_track
//                )
//            )
//        )
//
//        addSlide(
//            AppIntroFragment.newInstance(
//                SliderPage(
//                    description = "Keep track of the upcoming events in your workplace",
//                    backgroundColor = resources.getColor(R.color.intro_slide_3),
//                    imageDrawable = R.drawable.ic_greeting
//                )
//            )
//        )

        setTransformer(AppIntroPageTransformerType.Parallax())
//      isColorTransitionsEnabled = true
        setImmersiveMode()
    }

    public override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        finish()
    }

    public override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}