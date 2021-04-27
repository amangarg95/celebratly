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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.ActivityUploadDataBinding
import com.kiprosh.optimizeprime.helper.AuthenticationHelper
import com.kiprosh.optimizeprime.helper.BottomSheetDialog
import com.kiprosh.optimizeprime.helper.CommonCode
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UploadDataActivity : AppCompatActivity(), BottomSheetDialog.onItemClickListener {
    lateinit var uploadDataActivityBinding: ActivityUploadDataBinding
    lateinit var progressDialog: ProgressDialog
    lateinit var apiInterface: APIInterface
    private lateinit var authenticationHelper: AuthenticationHelper
    private var occurrenceId = 0
    var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        occurrenceId = intent.getIntExtra("OCCURRENCE_ID", 0)
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
        uploadDataActivityBinding.etGreeting.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                uploadDataActivityBinding.tvPreviewText.text = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
//
            }
        })

        uploadDataActivityBinding.btnUploadGreeting.setOnClickListener {
            callApi()

        }
    }

    private fun callApi() {
        val bitmap = CommonCode(this).getScreenShot(
            uploadDataActivityBinding.clPreview
        )
        val encodedString = CommonCode(this).getBase64FromBitmap(bitmap)
        val paramObject = JSONObject()
        paramObject.put("base64", "data:image/gif;base64,$encodedString") //Base64 image
        uploadData(paramObject.toString())
        finish()
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
        }
    }

    private fun uploadData(emailReq: String) {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] =
            "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MjYsImV4cCI6MTYyNDcwOTkyMH0.EXggM21bXPnfdT0olp0mbRo0VossAysyssu8ITT6Vsk"
        apiInterface.uploadData(headerMap, occurrenceId.toString(), emailReq).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
    }
}