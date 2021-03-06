package com.kiprosh.optimizeprime.helper

import android.content.Context
import com.google.gson.Gson
import com.kiprosh.optimizeprime.model.User
import com.kiprosh.optimizeprime.model.UserProfile

class AuthenticationHelper(var context: Context) {

    val keyName = "Information"
    val keyUser = "User"
    val keyIsFirstSignIn = "SignInStatus"

    fun saveUserProfile(userProfile: UserProfile?) {
        if (userProfile != null) {
            val userJsonString = Gson().toJson(userProfile.user)
            val spUser = context.getSharedPreferences(keyName, Context.MODE_PRIVATE)
            val editor = spUser.edit()
            editor.putString(keyUser, userJsonString)
            editor.apply()
        }
    }

    fun getHeaderMap(): Map<String, String>? {
        val headerMap = mutableMapOf<String, String>()
        val user = getUser()
        return if (user != null) {
            headerMap["Authorization"] = "Bearer " + user.token
            headerMap["content-type"] = "application/json"
            headerMap
        } else {
            null
        }
    }

    fun getAndroidSecretKeyHeaderMap(): Map<String, String>? {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Android-Secret"] = "RCGQCvLyVrnLYBQCaSg8D5hx"
        return headerMap
    }

    fun getUser(): User? {
        val spUser = context.getSharedPreferences(keyName, Context.MODE_PRIVATE)
        val userString = spUser.getString(keyUser, null)
        if (userString != null) {
            return Gson().fromJson(userString, User::class.java)
        }
        return null
    }

    fun saveSignInStatus(status: Int) {
        val spUser = context.getSharedPreferences(keyName, Context.MODE_PRIVATE)
        val editor = spUser.edit()
        editor.putInt(keyIsFirstSignIn, status)
        editor.apply()
    }
}