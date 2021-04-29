package com.kiprosh.optimizeprime.model

import com.google.gson.annotations.SerializedName

data class OccurrencesResponse(
    @SerializedName("allow_upload")
    var allowUpload: Boolean,
    @SerializedName("caption")
    var caption: String,
    @SerializedName("color")
    var color: String,
    @SerializedName("end_at")
    var endAt: String,
    @SerializedName("event_id")
    var eventId: Int,
    @SerializedName("event_type")
    var eventType: String,
    @SerializedName("expires_at")
    var expiresAt: String,
    @SerializedName("final_video_url")
    var finalVideoUrl: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("note")
    var note: String,
    @SerializedName("start_at")
    var startAt: String,
    @SerializedName("status_uploads")
    var statusUploads: List<StatusUpload>,
    @SerializedName("title")
    var title: String,
    @SerializedName("url")
    var url: String,
    @SerializedName("image_url")
    var imageUrl: String = "",
    @SerializedName("upload_time_distance_in_words")
    var uploadTimeDistanceInWords: String,
    @SerializedName("timestamp")
    var timestamp: Long,
    @SerializedName("action_text")
    var actionText: String,
    @SerializedName("photo_url")
    var photoUrl: String
)