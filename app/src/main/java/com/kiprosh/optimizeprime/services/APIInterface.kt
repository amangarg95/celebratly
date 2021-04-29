package com.kiprosh.optimizeprime.services

import com.kiprosh.optimizeprime.model.*
import retrofit2.Call
import retrofit2.http.*

interface APIInterface {

    @FormUrlEncoded
    @POST("/login.json")
    fun getTokenNew(
        @HeaderMap header: Map<String, String>,
        @Field("user[email]") email: String
    ): Call<UserProfile>

    @GET("/occurrences.json")
    fun getOccurrences(@HeaderMap header: Map<String, String>): Call<ArrayList<OccurrencesResponse>>

    @GET("/profile.json")
    fun getProfileWithOccurrences(@HeaderMap header: Map<String, String>): Call<ProfileAndOccurrencesResponse>


    @POST("/occurrences/{id}/image_upload.json")
    fun uploadData(
        @HeaderMap header: Map<String, String>,
        @Path("id") id: String,
        @Body encodedString: EncodedStringReq
    ): Call<UploadDataResponse>


    @PUT("/profile.json")
    fun updateProfile(
        @HeaderMap header: Map<String, String>,
        @Query("profile_url") profileUrl: String,
        @Query("fcm_token") fcmToken: String
    ): Call<User>
}