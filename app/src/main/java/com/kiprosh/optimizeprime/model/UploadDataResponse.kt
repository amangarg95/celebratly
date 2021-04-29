package com.kiprosh.optimizeprime.model

import com.google.gson.annotations.SerializedName

data class UploadDataResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("image_url")
    var image_url: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("user")
    var user: User
)