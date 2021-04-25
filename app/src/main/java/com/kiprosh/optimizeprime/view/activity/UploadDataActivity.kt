package com.kiprosh.optimizeprime.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.ActivityUploadData2Binding
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog
import java.io.ByteArrayOutputStream

class UploadDataActivity : AppCompatActivity() {
    lateinit var uploadDataActivityBinding: ActivityUploadData2Binding
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadDataActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_upload_data_2)
        uploadDataActivityBinding.lifecycleOwner = this
        uploadDataActivityBinding.llOpenCamera.setOnClickListener { takePhoto() }
        uploadDataActivityBinding.llOpenGallery.setOnClickListener { selectImageInAlbum() }
        updateStatusBarColour()
        progressDialog = ProgressDialog()
        uploadDataActivityBinding.etGreeting.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty()) {
                    uploadDataActivityBinding.tvPreviewText.visibility = VISIBLE
                    uploadDataActivityBinding.tvPreviewText.text = s.toString()
                } else {
//                    uploadDataActivityBinding.tvPreviewText.visibility = GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        uploadDataActivityBinding.tvUploadGreeting.setOnClickListener {
            progressDialog.showProgress(supportFragmentManager)
            Handler().postDelayed(Runnable {
                progressDialog.hideProgress()
                finish()
                Toast.makeText(this, "Greeting uploaded!", LENGTH_LONG).show()
            }, 5000)

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

    private fun takeScreenshot() {
        uploadDataActivityBinding.civOpenCamera.setImageBitmap(
            CommonCode(this).getScreenShot(
                uploadDataActivityBinding.clPreview
            )
        )
    }

    private fun updateStatusBarColour() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.intro_slide_2)
    }
}