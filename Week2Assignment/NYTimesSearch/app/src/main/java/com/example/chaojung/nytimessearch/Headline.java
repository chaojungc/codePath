package com.example.chaojung.nytimessearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ChaoJung on 2017/2/23.
 */

@Parcel
public class Headline {

    @SerializedName("main")
    @Expose
    String main;

    public Headline() {
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
