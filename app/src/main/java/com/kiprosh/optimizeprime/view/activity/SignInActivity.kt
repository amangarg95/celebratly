package com.kiprosh.optimizeprime.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.optimizeprimeandroidapp.model.EmailReq
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
import com.kiprosh.optimizeprime.UserProfile
import com.kiprosh.optimizeprime.databinding.ActivityLoginBinding
import com.kiprosh.optimizeprime.helper.AuthenticationHelper
import com.kiprosh.optimizeprime.model.User
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.googleButton.setOnClickListener(this)
        apiInterface = RetrofitClientInstance.getRetrofitInstance().create(APIInterface::class.java)
        authenticationHelper = AuthenticationHelper(applicationContext)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .setHostedDomain(getString(R.string.sign_in_domain))
            .requestIdToken(getString(R.string.auth_api_key))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
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
                    Log.d(
                        TAG,
                        "firebaseAuthWithGoogle:" + account.id + account.email + account.givenName
                    )
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                }
            } else {

            }
        }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private fun getAuthenticationToken(googleSignInAccount: GoogleSignInAccount) {
        val emailReq = EmailReq()
        emailReq.email = googleSignInAccount.email!!.toString()
        apiInterface.getTokenNew(googleSignInAccount.email!!.toString())
            .enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    if (response.isSuccessful) {
                        retrieveFcmToken(googleSignInAccount, response.body())
                        authenticationHelper.saveUserProfile(response.body())
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {

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
                        Toast.makeText(
                            applicationContext,
                            "Authenticate Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        openMainActivity()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {

                }
            })
        } else {

        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}