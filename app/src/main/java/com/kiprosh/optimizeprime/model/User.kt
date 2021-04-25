package com.kiprosh.optimizeprime.model

import com.google.gson.annotations.SerializedName

data class User(
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