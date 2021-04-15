package com.example.optimizeprimeandroidapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.optimizeprimeandroidapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var mainActivityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityMainBinding.lifecycleOwner = this
        setCurrentFragment(MyFeedFragment())
        mainActivityMainBinding.bnvMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_my_feed -> setCurrentFragment(MyFeedFragment())
                R.id.item_my_events -> setCurrentFragment(MyEventsFragment())
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