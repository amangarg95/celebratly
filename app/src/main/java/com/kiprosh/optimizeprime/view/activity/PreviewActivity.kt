package com.kiprosh.optimizeprime.view.activity

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.ActivityPreviewBinding
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog

class PreviewActivity : AppCompatActivity() {
    lateinit var previewBinding: ActivityPreviewBinding
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog()
        progressDialog.showProgress(supportFragmentManager)
        previewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_preview)
        previewBinding.lifecycleOwner = this
        updateStatusBarColour()
        CommonCode(this).loadImageFromUrl(
            previewBinding.ivPreview,
            intent.getStringExtra("IMAGE_URL"), progressDialog
        )
    }

    private fun updateStatusBarColour() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.intro_slide_2)
    }
}