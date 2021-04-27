package com.kiprosh.optimizeprime.model

import com.google.gson.annotations.SerializedName

data class OccurrencesResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("event_id")
    val eventId: Int,
    @SerializedName("expires_at")
    var expiresAt: String,
    @SerializedName("note")
    var note: String,
    @SerializedName("title")
    var titleText: String,
    @SerializedName("start_at")
    var startAt: String,
    @SerializedName("end_at")
    var endAt: String,
    @SerializedName("caption")
    var caption: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("event_type")
    var eventTyoe: String,
    @SerializedName("color")
    var color: String,
    @SerializedName("final_video_url")
    var videoUrl: String,
    @SerializedName("timestamp")
    var timestamp: Long
)