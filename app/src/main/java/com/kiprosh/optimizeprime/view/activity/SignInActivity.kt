package com.kiprosh.optimizeprime.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.kiprosh.optimizeprime.R
import com.kiprosh.optimizeprime.databinding.ActivityLoginBinding
import com.kiprosh.optimizeprime.helper.AuthenticationHelper
import com.kiprosh.optimizeprime.helper.ProgressDialog
import com.kiprosh.optimizeprime.model.User
import com.kiprosh.optimizeprime.model.UserProfile
import com.kiprosh.optimizeprime.services.APIInterface
import com.kiprosh.optimizeprime.view.adapter.RetrofitClientInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignInActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var apiInterface: APIInterface
    private lateinit var authenticationHelper: AuthenticationHelper
    private lateinit var progressBar: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.googleButton.setOnClickListener(this)
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        updateStatusBarColour()
        authenticationHelper = AuthenticationHelper(applicationContext)
        progressBar = ProgressDialog()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .setHostedDomain(getString(R.string.sign_in_domain))
            .requestIdToken(getString(R.string.auth_api_key))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleSignInClient.revokeAccess()
        googleSignInClient.signOut()
        auth = Firebase.auth
        Firebase.messaging.isAutoInitEnabled = true
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.google_button) {
            performGoogleSingIn()
        }
    }

    private fun performGoogleSingIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    getAuthenticationToken(account)
                } catch (e: ApiException) {
                    Toast.makeText(applicationContext, R.string.text_api_error, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }


    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private fun getAuthenticationToken(googleSignInAccount: GoogleSignInAccount) {
        progressBar.showProgress(supportFragmentManager)
        apiInterface.getTokenNew(googleSignInAccount.email!!.toString())
            .enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    if (response.isSuccessful) {
                        retrieveFcmToken(googleSignInAccount, response.body())
                        authenticationHelper.saveUserProfile(response.body())
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    progressBar.hideProgress()
                    Toast.makeText(applicationContext, R.string.text_api_error, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun retrieveFcmToken(
        googleSignInAccount: GoogleSignInAccount,
        userProfile: UserProfile?
    ) {
        if (userProfile != null) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { tokenTask ->
                run {
                    if (tokenTask.isSuccessful) {
                        updateAdditionalInformation(
                            googleSignInAccount,
                            tokenTask.result
                        )
                    }
                }
            }
        }
    }

    private fun updateAdditionalInformation(
        googleSignInAccount: GoogleSignInAccount,
        fcmToken: String?
    ) {
        val headerMap = authenticationHelper.getHeaderMap()
        if (headerMap != null) {
            apiInterface.updateProfile(
                headerMap,
                googleSignInAccount.photoUrl.toString(), fcmToken!!
            ).enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        progressBar.hideProgress()
                        Toast.makeText(
                            applicationContext,
                            "Authenticate Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        openMainActivity()
                    } else {
                        progressBar.hideProgress()
                        Toast.makeText(
                            applicationContext,
                            R.string.text_api_error,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    progressBar.hideProgress()
                    Toast.makeText(applicationContext, R.string.text_api_error, Toast.LENGTH_SHORT)
                        .show()
                }
            })
        } else {
            progressBar.hideProgress()
        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun updateStatusBarColour() {
        val window: Window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.pistachio_green)
    }
}