package com.example.optimizeprimeandroidapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.optimizeprimeandroidapp.R
import com.example.optimizeprimeandroidapp.databinding.ActivityLoginBinding
import com.example.optimizeprimeandroidapp.model.EmailReq
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.example.optimizeprimeandroidapp.model.UpcomingEventsResponse
import com.example.optimizeprimeandroidapp.services.APIInterface
import com.example.optimizeprimeandroidapp.view.adapter.RetrofitClientInstance
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 1
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var binding: ActivityLoginBinding
    lateinit var apiInterface: APIInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        updateProfile()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.googleButton.setOnClickListener {
            signIn()
        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if (account != null) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn() {
        val intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            redirectToMainActivity()
//            try {
//                val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)
//                redirectToMainActivity()
//            } catch (e: ApiException) {
//                Log.v("TAG","signInResult:failed code=" + e.statusCode)
//            }
        }
    }

    private fun redirectToMainActivity(){
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun getHeaderMap(accessToken: String): Map<String, String> {
        val headerMap: MutableMap<String, String> = HashMap()
        //headerMap["Authorization"] = "Bearer "+accessToken
        headerMap["Content-Type"] = "application/json"
        return headerMap
    }
    private fun getToken(){
        val headerMap: Map<String, String> = getHeaderMap("")
        val emailReq = EmailReq()
        emailReq.email = "kavita@kiprosh.com"
        apiInterface.getToken(emailReq, headerMap).enqueue(object :
            Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("token_testing", "1response-->" + response.toString())
            }

            override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                Log.d("token_testing", "throwable-->" + throwable.message)
            }
        })
    }

    private fun updateProfile(){

        apiInterface.updateProfile("https://avatars.githubusercontent.com/u/10562161?v=4").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("profile_testing", "1response-->" + response.toString())
            }

            override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                Log.d("profile_testing", "throwable-->" + throwable.message)
            }
        })
    }
}