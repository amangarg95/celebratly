package com.kiprosh.optimizeprime.services;

import com.kiprosh.optimizeprime.UserProfile;
import com.kiprosh.optimizeprime.model.UpcomingEventsResponse;

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
