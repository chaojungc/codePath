package com.codepath.apps.simpletweets.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ChaoJung on 2017/3/5.
 */

@Parcel
public class Entities {

    public Entities() {
    }

    public List<Medium> getMedia() {
        return media;
    }

    @SerializedName("media")
    @Expose
    public List<Medium> media = null;
}
