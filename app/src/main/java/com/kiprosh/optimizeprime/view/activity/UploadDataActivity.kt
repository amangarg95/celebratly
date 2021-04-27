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
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.ActivityUploadDataBinding
import com.kiprosh.optimizeprime.helper.BottomSheetDialog
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog
import java.io.ByteArrayOutputStream


class UploadDataActivity : AppCompatActivity(), BottomSheetDialog.onItemClickListener {
    lateinit var uploadDataActivityBinding: ActivityUploadDataBinding
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadDataActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_upload_data)
        uploadDataActivityBinding.lifecycleOwner = this
        uploadDataActivityBinding.llOpenCamera.setOnClickListener { takePhoto() }
        uploadDataActivityBinding.llOpenGallery.setOnClickListener { selectImageInAlbum() }
        updateStatusBarColour()
        progressDialog = ProgressDialog()
        uploadDataActivityBinding.civCustomBackground.setOnClickListener {
            BottomSheetDialog(this).show(supportFragmentManager, " ModalBottomSheet")
        }
        uploadDataActivityBinding.etGreeting.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                uploadDataActivityBinding.tvPreviewText.text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        uploadDataActivityBinding.btnUploadGreeting.setOnClickListener {
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
            uploadDataActivityBinding.ivPreview.setImageURI(data?.data)
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_TAKE_PHOTO && data != null) {
            uploadDataActivityBinding.ivPreview.setImageBitmap(data.extras?.get("data") as Bitmap)
            val bitmap = (uploadDataActivityBinding.ivPreview.drawable as BitmapDrawable).bitmap
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

    override fun onItemClick(pos: Int) {
        when (pos) {
            1 -> {
                uploadDataActivityBinding.ivPreview.background = getDrawable(R.drawable.ic_cool)
            }
            2 -> {
                uploadDataActivityBinding.ivPreview.background = getDrawable(R.drawable.ic_summer)
            }
            3 -> {
                uploadDataActivityBinding.ivPreview.background = getDrawable(R.drawable.ic_night)
            }
        }
    }
}