package com.kiprosh.optimizeprime.services

import com.kiprosh.optimizeprime.model.EmailReq
import com.kiprosh.optimizeprime.model.OccurrencesResponse
import com.kiprosh.optimizeprime.model.UpcomingEventsResponse
import com.kiprosh.optimizeprime.model.User
import com.kiprosh.optimizeprime.model.UserProfile
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

    @POST("/occurrences/{id}/image_upload")
    fun uploadData(@HeaderMap header: Map<String, String>, @Path("id") id: String, @Body body: String): Call<ResponseBody>


    @FormUrlEncoded
    @PUT("/profile.json")
    fun updateProfile(
        @HeaderMap header: Map<String, String>,
        @Field("profile_url") profileUrl: String,
        @Field("fcm_token") fcmToken: String
    ): Call<User>
}