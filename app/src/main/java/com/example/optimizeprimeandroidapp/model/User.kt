package com.example.optimizeprimeandroidapp.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("profile_url")
    val profileUrl: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("dob")
    val dob: String,
    @SerializedName("doj")
    val doj: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("id")
    val id: Int
)