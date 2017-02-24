package com.example.chaojung.nytimessearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ChaoJung on 2017/2/23.
 */

public class Response {

    @SerializedName("docs")
    @Expose
    private List<Doc> docs = null;

    public List<Doc> getDocs() {
        return docs;
    }

    public void setDocs(List<Doc> docs) {
        this.docs = docs;
    }

}
