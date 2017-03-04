package com.codepath.apps.simpletweets.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ChaoJung on 2017/3/3.
 */

@Parcel
public class User {

    public User() {
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("id")
    @Expose
    public long uid;
    @SerializedName("screen_name")
    @Expose
    public String screenName;
    @SerializedName("profile_image_url")
    @Expose
    public String profileImageUrl;
}
