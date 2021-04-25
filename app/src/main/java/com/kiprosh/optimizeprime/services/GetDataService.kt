package com.kiprosh.optimizeprime.services

import com.kiprosh.optimizeprime.UserProfile
import okhttp3.ResponseBody
import retrofit2.Call

interface GetDataService {

    fun getUser(apiUrl: String, email: String): Call<UserProfile>

    fun getUpcomingEvents(): Call<ResponseBody>

    fun getOccurrences(): Call<ResponseBody>
}
