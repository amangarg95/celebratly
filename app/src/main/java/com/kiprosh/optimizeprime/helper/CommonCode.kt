package com.kiprosh.optimizeprime.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.Base64
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kiprosh.optimizeprime.R
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

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

    fun getBase64FromBitmap(bitmap: Bitmap): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}