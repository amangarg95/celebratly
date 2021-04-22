package com.example.optimizeprimeandroidapp

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.optimizeprimeandroidapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var mainActivityMainBinding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityMainBinding.lifecycleOwner = this
        setCurrentFragment(MyFeedFragment())
        mainActivityMainBinding.bnvMenu.addBubbleListener {
            when (it) {
                R.id.item_my_feed -> setCurrentFragment(MyFeedFragment())
                R.id.item_upcoming_events -> setCurrentFragment(UpcomingEventsFragment())
                R.id.item_my_profile -> setCurrentFragment(MyProfileFragment())
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            commit()
        }
}