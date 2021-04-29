package com.kiprosh.optimizeprime.services

import com.kiprosh.optimizeprime.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @FormUrlEncoded
    @POST("/login.json")
    fun getTokenNew(@Field("user[email]") email: String): Call<UserProfile>

    @GET("/occurrences.json")
    fun getOccurrences(): Call<ArrayList<OccurrencesResponse>>


    @POST("/occurrences/{id}/image_upload.json")
    fun uploadData(
        @HeaderMap header: Map<String, String>,
        @Path("id") id: String,
        @Body encodedString: EncodedStringReq
    ): Call<UploadDataResponse>


    @FormUrlEncoded
    @PUT("/profile.json")
    fun updateProfile(
        @HeaderMap header: Map<String, String>,
        @Field("profile_url") profileUrl: String,
        @Field("fcm_token") fcmToken: String
    ): Call<User>
}