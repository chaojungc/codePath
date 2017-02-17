package com.example.chaojung.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ChaoJung on 2017/2/18.
 */

public class Trailer {

    public String getKey() {
        return key;
    }

    String key;
    String site;
    String type;

    public Trailer(JSONObject jsonObject) throws JSONException {

        this.key = jsonObject.getString("key");
        this.site = jsonObject.getString("site");
        this.type = jsonObject.getString("type");
    }

    public static ArrayList<Trailer> transJSONArray(JSONArray array) throws JSONException {

        ArrayList<Trailer> results = new ArrayList<>();

        for(int i = 0; i < array.length();i++) {
            results.add(new Trailer(array.getJSONObject(i)));
        }

        return results;

    }
}
