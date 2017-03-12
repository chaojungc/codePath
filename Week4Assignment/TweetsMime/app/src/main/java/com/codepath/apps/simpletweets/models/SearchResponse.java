package com.codepath.apps.simpletweets.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ChaoJung on 2017/3/12.
 */

@Parcel
public class SearchResponse {

    public SearchResponse() {
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    @SerializedName("statuses")
    @Expose
    public List<Tweet> tweets = null;

}
