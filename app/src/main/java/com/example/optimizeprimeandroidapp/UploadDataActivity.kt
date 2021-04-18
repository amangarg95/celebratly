package com.example.optimizeprimeandroidapp

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.optimizeprimeandroidapp.databinding.ActivityUploadDataBinding
import java.io.ByteArrayOutputStream

class UploadDataActivity : AppCompatActivity() {
    lateinit var uploadDataActivityBinding: ActivityUploadDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadDataActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_upload_data)
        uploadDataActivityBinding.lifecycleOwner = this
        uploadDataActivityBinding.tvOpenCamera.setOnClickListener { takePhoto() }
        uploadDataActivityBinding.tvOpenGallery.setOnClickListener { selectImageInAlbum() }

        uploadDataActivityBinding.llSummer.setOnClickListener {
            changeBackground(0)
        }

        uploadDataActivityBinding.llCool.setOnClickListener {
            changeBackground(1)
        }

        uploadDataActivityBinding.llNight.setOnClickListener {
            changeBackground(2)
        }

        uploadDataActivityBinding.llDesert.setOnClickListener {
            changeBackground(3)
        }
    }

    private fun changeBackground(position: Int) {
        when (position) {
            0 -> {
                uploadDataActivityBinding.ivPreview.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_summer)
            }
            1 -> {
                uploadDataActivityBinding.ivPreview.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_cool)
            }
            2 -> {
                uploadDataActivityBinding.ivPreview.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_night)
            }
            3 -> {
                uploadDataActivityBinding.ivPreview.background =
                    ContextCompat.getDrawable(this, R.drawable.ic_desert)
            }
        }
    }

    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
    }

    private fun takePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO)
    }

    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM) {
            uploadDataActivityBinding.ivPreview.setImageURI(data?.data) // handle chosen image
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_PHOTO && data != null) {
            uploadDataActivityBinding.ivPreview.setImageBitmap(data.extras?.get("data") as Bitmap)
            var bitmap = (uploadDataActivityBinding.ivPreview.drawable as BitmapDrawable).bitmap
            getEncodedBitmapString(bitmap)
        }
    }

    private fun getEncodedBitmapString(photo: Bitmap): String {
        val bAOS = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 60, bAOS)
        return Base64.encodeToString(bAOS.toByteArray(), Base64.DEFAULT)
    }
}