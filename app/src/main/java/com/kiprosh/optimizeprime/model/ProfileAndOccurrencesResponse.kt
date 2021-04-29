package com.kiprosh.optimizeprime.model

import com.google.gson.annotations.SerializedName

data class ProfileAndOccurrencesResponse(
    @SerializedName("occurrences")
    var listOfOccurrences: ArrayList<OccurrencesResponse>,

    @SerializedName("profile_url")
    val profileUrl: String,
    @SerializedName("fcm_token")
    val fcmToken: String,
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