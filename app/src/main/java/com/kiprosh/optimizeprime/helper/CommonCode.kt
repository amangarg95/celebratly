package com.kiprosh.optimizeprime.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kiprosh.optimizeprime.R
import de.hdodenhof.circleimageview.CircleImageView

class CommonCode(val context: Context) {

    fun loadUserProfileImage(civUserProfile: CircleImageView, profileUrl: String?) {
        Glide.with(context).load(profileUrl)
            .apply(RequestOptions().placeholder(R.drawable.no_profile).error(R.drawable.no_profile))
            .into(civUserProfile)
    }

    fun getScreenShot(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) bgDrawable.draw(canvas)
        else canvas.drawColor(Color.WHITE)
        view.draw(canvas)
        return returnedBitmap
    }
}