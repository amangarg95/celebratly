package com.example.optimizeprimeandroidapp.helper

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.optimizeprimeandroidapp.R
import de.hdodenhof.circleimageview.CircleImageView

class CommonCode(val context: Context) {

    fun loadUserProfileImage(civUserProfile: CircleImageView, profileUrl: String) {
        Glide.with(context).load(profileUrl)
            .apply(RequestOptions().placeholder(R.drawable.no_profile).error(R.drawable.no_profile))
            .into(civUserProfile)
    }


}