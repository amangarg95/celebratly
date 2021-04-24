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
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("url")
    val url: String
)