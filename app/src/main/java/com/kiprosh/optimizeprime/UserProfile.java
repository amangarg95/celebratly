package com.example.optimizeprimeandroidapp;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("dob")
    private String albumId;
    @SerializedName("doj")
    private String id;
    @SerializedName("email")
    private String title;
    @SerializedName("full_name")
    private String url;
    @SerializedName("id")
    private Integer thumbnailUrl;

    public UserProfile(String albumId, String id, String title, String url, Integer thumbnailUrl) {
        this.albumId = albumId;
        this.id = id;
        this.title = title;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(Integer thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}