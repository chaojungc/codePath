package com.example.chaojung.nytimessearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ChaoJung on 2017/2/23.
 */

public class Multimedium {

    @SerializedName("url")
    @Expose
    private String url;

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
