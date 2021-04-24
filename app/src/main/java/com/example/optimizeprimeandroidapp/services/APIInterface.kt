package com.example.optimizeprimeandroidapp.services

import com.example.optimizeprimeandroidapp.UserProfile
import com.example.optimizeprimeandroidapp.model.EmailReq
import com.example.optimizeprimeandroidapp.model.OccurrencesResponse
import com.example.optimizeprimeandroidapp.model.UpcomingEventsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @POST("/login.json")
    fun getToken(@Body email: EmailReq, @HeaderMap headerMap: Map<String, String>): Call<ResponseBody>

    @FormUrlEncoded
    @POST
    fun getTokenNew(@Url apiUrl: String, @Field("user[email]") email: String): Call<ResponseBody>

    @GET("/events")
    fun getUpcomingEvents(): Call<ArrayList<UpcomingEventsResponse>>

    @GET("/occurrences")
    fun getOccurrences(): Call<ArrayList<OccurrencesResponse>>

    @FormUrlEncoded
    @PUT("/profile")
    fun updateProfile(@Field("profile_url") profileUrl: String): Call<ResponseBody>
}