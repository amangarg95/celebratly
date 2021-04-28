package com.kiprosh.optimizeprime.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.ActivityUploadDataBinding
import com.kiprosh.optimizeprime.helper.BottomSheetDialog
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.pixelcarrot.base64image.Base64Image
import java.io.ByteArrayOutputStream


class UploadDataActivity : AppCompatActivity(), BottomSheetDialog.onItemClickListener,
    View.OnTouchListener,
    View.OnDragListener {
    lateinit var uploadDataActivityBinding: ActivityUploadDataBinding
    lateinit var progressDialog: ProgressDialog
    var base64String = ""
    private val TAG = "OP"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uploadDataActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_upload_data)
        uploadDataActivityBinding.lifecycleOwner = this
        uploadDataActivityBinding.llOpenCamera.setOnClickListener { takePhoto() }
        uploadDataActivityBinding.llOpenGallery.setOnClickListener { selectImageInAlbum() }
        updateStatusBarColour()
        progressDialog = ProgressDialog()
        uploadDataActivityBinding.tvPreviewText.setOnTouchListener(this)
        uploadDataActivityBinding..setOnDragListener(this)
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
            var bitmap = CommonCode(this).getScreenShot(
                uploadDataActivityBinding.clPreview
            )
            Base64Image.encode(bitmap) { base64 ->
                base64?.let {
                    base64String = it
                }
            }
            finish()
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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return if (event!!.action === MotionEvent.ACTION_DOWN) {
            val dragShadowBuilder = View.DragShadowBuilder(v)
            v!!.startDrag(null, dragShadowBuilder, v, 0)
            true
        } else {
            false
        }
    }

    override fun onDrag(v: View?, event: DragEvent?): Boolean {
        when (event!!.action) {
            DragEvent.ACTION_DRAG_ENDED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENDED ")
                return true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_EXITED")
                return true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_ENTERED")
                return true
            }
            DragEvent.ACTION_DRAG_STARTED -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_STARTED")
                return true
            }
            DragEvent.ACTION_DROP -> {
                Log.d(TAG, "onDrag: ACTION_DROP")
                val tvState = event.localState as View
                Log.d(TAG, "onDrag:viewX" + event.x + "viewY" + event.y)
                Log.d(TAG, "onDrag: Owner->" + tvState.parent)
                val tvParent = tvState.parent as ViewGroup
                tvParent.removeView(tvState)
                val container = v as ConstraintLayout
                container.addView(tvState)
                tvParent.removeView(tvState)
                tvState.x = event.x
                tvState.y = event.y
                v.addView(tvState)
                v.setVisibility(View.VISIBLE)
                return true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                Log.d(TAG, "onDrag: ACTION_DRAG_LOCATION")
                return true
            }
            else -> return false
        }
    }
}