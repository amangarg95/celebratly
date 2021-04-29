package com.kiprosh.optimizeprime.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.ActivityUploadDataBinding
import com.kiprosh.optimizeprime.helper.AuthenticationHelper
import com.kiprosh.optimizeprime.helper.BottomSheetDialog
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.model.EncodedStringReq
import com.kiprosh.optimizeprime.model.UploadDataResponse
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UploadDataActivity : AppCompatActivity(), BottomSheetDialog.onItemClickListener {
    lateinit var uploadDataActivityBinding: ActivityUploadDataBinding
    lateinit var progressDialog: ProgressDialog
    private lateinit var apiInterface: APIInterface
    private lateinit var authenticationHelper: AuthenticationHelper
    private var occurrenceId = 0
    private var nameOfAssociate = ""
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        occurrenceId = intent.getIntExtra("OCCURRENCE_ID", 0)
        nameOfAssociate = intent.getStringExtra("ASSOCIATE_NAME").toString()
        userName = intent.getStringExtra("USER_NAME").toString()
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        authenticationHelper = AuthenticationHelper(applicationContext)
        uploadDataActivityBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_upload_data)
        uploadDataActivityBinding.lifecycleOwner = this
        uploadDataActivityBinding.llOpenCamera.setOnClickListener { takePhoto() }
        uploadDataActivityBinding.llOpenGallery.setOnClickListener { selectImageInAlbum() }
        updateStatusBarColour()
        progressDialog = ProgressDialog()
        uploadDataActivityBinding.tvAssociateName.text =
            "@" + userName.partition { it in " " }.second
        uploadDataActivityBinding.civCustomBackground.setOnClickListener {
            BottomSheetDialog(this).show(supportFragmentManager, " ModalBottomSheet")
        }
        uploadDataActivityBinding.tvPreviewText.text = "Text Preview Here"

        if(nameOfAssociate.isNullOrEmpty()){
            uploadDataActivityBinding.etGreeting.hint = "Click here to add wishes..."
        }else{
            uploadDataActivityBinding.etGreeting.hint = "Click here to add wishes for $nameOfAssociate"
        }


        uploadDataActivityBinding.etGreeting.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                uploadDataActivityBinding.tvPreviewText.text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
//
            }
        })

        uploadDataActivityBinding.btnUploadGreeting.setOnClickListener {
            if (uploadDataActivityBinding.etGreeting.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please add a wish!", LENGTH_LONG).show()
            } else {
                callApi()
            }
        }
    }

    private fun callApi() {
        progressDialog.showProgress(supportFragmentManager)
        val bitmap = CommonCode(this).getScreenShot(
            uploadDataActivityBinding.clPreview
        )
        val encodedString = CommonCode(this).getBase64FromBitmap(bitmap)
        val paramObject = JSONObject()
        paramObject.put("base64", encodedString) //Base64 image
        uploadData(encodedString!!)
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
        }
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
            4 -> {
                uploadDataActivityBinding.ivPreview.background = getDrawable(R.drawable.bg_flowers)
            }
            5 -> {
                uploadDataActivityBinding.ivPreview.background = getDrawable(R.drawable.bg_friends)
            }
            6 -> {
                uploadDataActivityBinding.ivPreview.background = getDrawable(R.drawable.bg_colours)
            }
        }
    }

    private fun uploadData(encodedString: String) {
        val headerMap = AuthenticationHelper(this).getHeaderMap()
        val encodedStringReq = EncodedStringReq()
        encodedStringReq.email = encodedString
        apiInterface.uploadData(headerMap!!, occurrenceId.toString(), encodedStringReq)
            .enqueue(object :
                Callback<UploadDataResponse> {
                override fun onResponse(
                    call: Call<UploadDataResponse>,
                    response: Response<UploadDataResponse>
                ) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Hooray! Upload successful!",
                        LENGTH_LONG
                    ).show()
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

                override fun onFailure(call: Call<UploadDataResponse>, t: Throwable) {
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Upload failed! Please try again...",
                        LENGTH_LONG
                    ).show()
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            })
    }
}