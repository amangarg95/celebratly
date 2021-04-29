package com.kiprosh.optimizeprime.model

import com.google.gson.annotations.SerializedName

data class StatusUpload(
    @SerializedName("id")
    var id: Int,
    @SerializedName("image_url")
    var imageUrl: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("user")
    var user: User
)