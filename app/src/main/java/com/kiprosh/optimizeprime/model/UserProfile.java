package com.kiprosh.optimizeprime.model;

import com.google.gson.annotations.SerializedName;
import com.kiprosh.optimizeprime.model.User;

public class UserProfile {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "user=" + user +
                '}';
    }
}