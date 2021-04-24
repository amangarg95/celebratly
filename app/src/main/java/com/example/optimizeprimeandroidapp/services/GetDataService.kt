package com.example.optimizeprimeandroidapp.services

import com.example.optimizeprimeandroidapp.UserProfile
import com.example.optimizeprimeandroidapp.model.UpcomingEventsResponse
import com.example.optimizeprimeandroidapp.model.User
import retrofit2.Call
import retrofit2.http.*

interface GetDataService {
    /*@get:GET("/users.json")
    val userList: Call<List<User?>?>?
*/
    @FormUrlEncoded
    @POST
    fun getUser(@Url apiUrl: String?, @Field("user[email]") email: String?): Call<UserProfile?>

    @get:GET("/events")
    val upcomingEvents: Call<List<UpcomingEventsResponse?>?>?
}