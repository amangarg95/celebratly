package com.kiprosh.optimizeprime.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
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

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId: Int =
            context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun dpToPx(dp: Int): Int {
        val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun shareTextWithAnotherApp(context: Context, textToBeShared: String?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(Intent.EXTRA_TEXT, textToBeShared)
        context.startActivity(
            Intent.createChooser(
                sharingIntent,
                "Share via"
            )
        )
    }
}