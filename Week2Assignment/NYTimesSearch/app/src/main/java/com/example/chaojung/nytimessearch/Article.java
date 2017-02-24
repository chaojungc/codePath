package com.example.chaojung.nytimessearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ChaoJung on 2017/2/20.
 */

public class Article {

    @SerializedName("response")
    @Expose
    private Response response;

    @SerializedName("copyright")
    @Expose
    private String copyright;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Article() {
        copyright = "";
        response = new Response();
    }

    public static Article parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        Article article = gson.fromJson(response, Article.class);
        return article;
    }

}
  /* public Article(JSONObject jsonObject) throws JSONException {
        this.webUrl = jsonObject.getString("web_url");
        this.headline = jsonObject.getJSONObject("headline").getString("main");

        JSONArray multimedia = jsonObject.getJSONArray("multimedia");
        if(multimedia.length() > 0){
            JSONObject multimediaJSON = multimedia.getJSONObject(0);
            this.thumbNail = "https://www.nytimes.com/" + multimediaJSON.getString("url");
        }
        else{
            this.thumbNail = "";
        }
    }*/
