package com.example.chaojung.nytimessearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ChaoJung on 2017/2/23.
 */

public class Headline {

    @SerializedName("main")
    @Expose
    private String main;

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
