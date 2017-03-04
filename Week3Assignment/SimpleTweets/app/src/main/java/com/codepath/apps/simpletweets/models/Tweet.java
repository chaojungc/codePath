package com.codepath.apps.simpletweets.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ChaoJung on 2017/3/3.
 */
@Parcel
public class Tweet {

    public Tweet() {
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public Entities getEntities() {
        return entities;
    }

    // list out the attributes
    @SerializedName("text")
    @Expose
    public String body;
    @SerializedName("id")
    @Expose
    public long uid; //unique id for tweets
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("favourites_count")
    @Expose
    public int favouritesCount;
    @SerializedName("retweet_count")
    @Expose
    public int retweetCount;
    @SerializedName("entities")
    @Expose
    public Entities entities;
}
