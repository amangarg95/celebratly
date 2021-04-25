package com.example.optimizeprimeandroidapp.services;

import com.example.optimizeprimeandroidapp.UserProfile;
import com.example.optimizeprimeandroidapp.model.UpcomingEventsResponse;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import retrofit2.Call;

class GetDataServiceImpl implements GetDataService {

    @Nullable
    @Override
    public Call<List<UpcomingEventsResponse>> getUpcomingEvents() {
        return null;
    }

    @NotNull
    @Override
    public Call<UserProfile> getUser(@Nullable String apiUrl, @Nullable String email) {
        return null;
    }
}
