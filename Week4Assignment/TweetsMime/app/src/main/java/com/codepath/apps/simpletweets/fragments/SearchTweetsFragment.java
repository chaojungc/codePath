package com.codepath.apps.simpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.simpletweets.models.SearchResponse;
import com.codepath.apps.simpletweets.utils.TwitterApplication;
import com.codepath.apps.simpletweets.utils.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ChaoJung on 2017/3/12.
 */

public class SearchTweetsFragment extends TweetsListFragment{

    private TwitterClient client;
    private SearchResponse responseTweet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient(); //singleton client
        populateTimeline(defaultMaxID, 0);

    }

    public static SearchTweetsFragment newInstance(String query){
        SearchTweetsFragment frag = new SearchTweetsFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void populateTimeline(long page, final int source) {

        String query = getArguments().getString("query");

        client.getSearchTweets(page, query, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                //source = 1 means calling from swipeToRefresh
                if(source == 1){ getTweetsAdapter().clear(); }

                Gson gson = new GsonBuilder().create();
                responseTweet = new SearchResponse();
                responseTweet = gson.fromJson(response, SearchResponse.class);
                getTweetsAdapter().getListItems().addAll(responseTweet.getTweets());
                getTweetsAdapter().notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                Log.d("DEBUG",error.toString());
            }
        });
    }


}
