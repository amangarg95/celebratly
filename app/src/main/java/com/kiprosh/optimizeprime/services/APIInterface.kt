package com.kiprosh.optimizeprime.services

import com.example.optimizeprimeandroidapp.model.EmailReq
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.kiprosh.optimizeprime.model.UserProfile
import com.kiprosh.optimizeprime.model.UpcomingEventsResponse
import com.kiprosh.optimizeprime.model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @POST("/login.json")
    fun getToken(@Body email: EmailReq): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/login.json")
    fun getTokenNew(@Field("user[email]") email: String): Call<UserProfile>

    @GET("/events")
    fun getUpcomingEvents(): Call<ArrayList<UpcomingEventsResponse>>

    @GET("/occurrences.json")
    fun getOccurrences(): Call<ArrayList<OccurrencesResponse>>

    @FormUrlEncoded
    @PUT("/profile.json")
    fun updateProfile(
        @HeaderMap header: Map<String, String>,
        @Field("profile_url") profileUrl: String,
        @Field("fcm_token") fcmToken: String
    ): Call<User>
}