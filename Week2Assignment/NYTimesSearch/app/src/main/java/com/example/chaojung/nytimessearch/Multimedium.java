package com.example.chaojung.nytimessearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ChaoJung on 2017/2/23.
 */

@Parcel
public class Multimedium {

    @SerializedName("url")
    @Expose
    String url;

    public Multimedium() {

    }

    public String getUrl() {
        if(url != null)
            return "http://www.nytimes.com/"+url;
        else
            return "";
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
