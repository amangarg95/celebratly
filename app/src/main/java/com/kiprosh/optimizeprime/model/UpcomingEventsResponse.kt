package com.kiprosh.optimizeprime.model

import com.google.gson.annotations.SerializedName

data class UpcomingEventsResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("start_at")
    val startAt: String,
    @SerializedName("end_at")
    val endAt: String,
    @SerializedName("color")
    val color: String
)