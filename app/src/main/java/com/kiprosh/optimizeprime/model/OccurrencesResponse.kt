package com.example.optimizeprimeandroidapp.model

import com.google.gson.annotations.SerializedName

data class OccurrencesResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("event_id")
    val eventId: Int,
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("start_at")
    val startAt: String,
    @SerializedName("end_at")
    val endAt: String,
    @SerializedName("caption")
    val caption: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("event_type")
    val eventTyoe: String,
    @SerializedName("color")
    val color: String
)