package com.example.optimizeprimeandroidapp.view.adapter;

import com.example.optimizeprimeandroidapp.User;
import com.example.optimizeprimeandroidapp.UserProfile;
import com.example.optimizeprimeandroidapp.dummy.RetroPhoto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface GetDataService {

    @GET("/photos")
    Call<List<RetroPhoto>> getAllPhotos();

    @GET("/users")
    Call<List<User>> getUserList();

//    @POST("/users")
//    Call<List<User>> getUser();

    @FormUrlEncoded
    @POST()
    Call<UserProfile> getUser(@Url String apiUrl, @Field("user[email]") String email);
}