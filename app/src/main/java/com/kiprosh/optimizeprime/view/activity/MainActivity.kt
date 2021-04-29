package com.kiprosh.optimizeprime.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.ActivityMainBinding
import com.kiprosh.optimizeprime.view.fragment.FeedFragment
import com.kiprosh.optimizeprime.view.fragment.MyProfileFragment
import com.kiprosh.optimizeprime.view.fragment.UpcomingEventsFragment


class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainActivityMainBinding.lifecycleOwner = this
        setCurrentFragment(FeedFragment())
        mainActivityMainBinding.bnvMenu.addBubbleListener {
            when (it) {
                R.id.item_my_feed -> setCurrentFragment(FeedFragment())
                R.id.item_upcoming_events -> setCurrentFragment(UpcomingEventsFragment())
                R.id.item_my_profile -> setCurrentFragment(MyProfileFragment())
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            commit()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}