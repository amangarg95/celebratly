package com.kiprosh.optimizeprime.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Base64
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kiprosh.optimizeprime.R
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class CommonCode(val context: Context) {

    fun loadUserProfileImage(civUserProfile: CircleImageView, profileUrl: String?) {
        Glide.with(context).load(profileUrl)
            .apply(RequestOptions().placeholder(R.drawable.no_profile).error(R.drawable.no_profile))
            .into(civUserProfile)
    }

    fun loadImageFromUrl(
        imageView: AppCompatImageView,
        url: String?,
        progressDialog: ProgressDialog
    ) {
        var httpsString = url?.replace("http", "https", true)
        Glide.with(context).load(httpsString)
            .apply(RequestOptions().error(R.drawable.ic_no_data))
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressDialog.hideProgress()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressDialog.hideProgress()
                    return false
                }
            })
            .into(imageView)
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

    fun loadImageWithUrl(profileUrl: String, drawableId: Int, imageView: AppCompatImageView) {
        if (drawableId == 0) {
            Glide.with(context).load(profileUrl)
                .into(imageView)
        } else {
            Glide.with(context).load(profileUrl)
                .apply(RequestOptions().placeholder(drawableId).error(drawableId))
                .into(imageView)
        }
    }
}