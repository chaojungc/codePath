package com.example.chaojung.nytimessearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ChaoJung on 2017/2/23.
 */

@Parcel
public class Doc {

    @SerializedName("web_url")
    @Expose
    String webUrl;
    @SerializedName("multimedia")
    @Expose
    List<Multimedium> multimedia = null;
    @SerializedName("headline")
    @Expose
    Headline headline;

    public Doc(){
    }

    public Doc(Headline headline, List<Multimedium> multimedia, String webUrl) {
        this.headline = headline;
        this.multimedia = multimedia;
        this.webUrl = webUrl;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<Multimedium> getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(List<Multimedium> multimedia) {
        this.multimedia = multimedia;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }
}
