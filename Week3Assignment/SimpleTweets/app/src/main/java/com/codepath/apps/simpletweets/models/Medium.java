package com.codepath.apps.simpletweets.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ChaoJung on 2017/3/5.
 */
@Parcel
public class Medium {

    public Medium() {
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public long getId() {
        return id;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("media_url")
    @Expose
    public String mediaUrl;
    @SerializedName("display_url")
    @Expose
    public String displayUrl;
    @SerializedName("id")
    @Expose
    public long id;
    @SerializedName("expanded_url")
    @Expose
    public String expandedUrl;
}
