package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.utils.TwitterApplication;
import com.codepath.apps.simpletweets.utils.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ChaoJung on 2017/3/9.
 */

public class HomeTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

//    public static HomeTimelineFragment newInstance(Tweet updateTweet){
//        HomeTimelineFragment frag = new HomeTimelineFragment();
//        Bundle args = new Bundle();
//        //args.putString("updateTweet", updateTweet);
//        args.putParcelable("updateTweet", Parcels.wrap(updateTweet));
//        frag.setArguments(args);
//        return frag;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); //singleton client
        populateTimeline(defaultMaxID, 0);

    }

    @Override
    public void populateTimeline(long page, final int source) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //source = 1 means calling from swipeToRefresh
                // Remember to CLEAR OUT old items before appending in the new ones
                if(source == 1){ getTweetsAdapter().clear(); }
                // Define Response class to correspond to the JSON response returned
                Type collectionType = new TypeToken<List<Tweet>>(){}.getType();
                Gson gson = new GsonBuilder().create();
                getTweetsAdapter().getListItems().addAll((Collection<? extends Tweet>) gson.fromJson(response.toString(),collectionType));
                //tweets = (List<Tweet>) new Gson().fromJson(response.toString(),collectionType);
                getTweetsAdapter().notifyDataSetChanged();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("DEBUG", "Fetch timeline error: " + t.toString());
            }
        });
    }

}
