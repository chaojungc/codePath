package com.example.chaojung.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ChaoJung on 2017/2/14.
 */

public class Movie {

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s",backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getVoteAverage() { return voteAverage; }

    public String getId() { return id; }

    public String getReleaseDate() {
        return releaseDate;
    }


    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    String voteAverage;
    String id;
    String releaseDate;

    public Movie(JSONObject jsonObject) throws JSONException {

        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = jsonObject.getString("vote_average");
        this.id = jsonObject.getString("id");
        this.releaseDate = jsonObject.getString("release_date");
    }

    public static ArrayList<Movie> transJSONArray(JSONArray array) throws JSONException {

        ArrayList<Movie> results = new ArrayList<>();

        for(int i = 0; i < array.length();i++) {
            results.add(new Movie(array.getJSONObject(i)));
        }

        return results;

    }
}
