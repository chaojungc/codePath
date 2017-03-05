package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.simpletweets.ComposeTweetDialogFragment;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TweetsAdapter;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.utils.ItemClickSupport;
import com.codepath.apps.simpletweets.utils.TwitterApplication;
import com.codepath.apps.simpletweets.utils.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogListener {

   //private ActivityTimelineBinding binding;

    Gson gson = new GsonBuilder().create();

    public TwitterClient client;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;

    TweetsAdapter tweetsAdapter;
    RecyclerView rvTweets;
    FloatingActionButton fabCompose;
    User loginUser;

    List<Tweet> tweets;
    long defaultMaxID = 0;

    @Override
    public void onFinishComposeDialog(String update) {
        client.postTweet(update, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                tweets.add(0,gson.fromJson(responseBody,Tweet.class));
                tweetsAdapter.notifyItemInserted(0);
                scrollToTop();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
            }
        });
    }

    private void scrollToTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvTweets
                .getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //binding = DataBindingUtil.setContentView(this, R.layout.activity_timeline);
        client = TwitterApplication.getRestClient(); //singleton client

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        setupViews();
        populateTimeline(defaultMaxID, 0);
        composeTweet();
        swipeToRefresh();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void setupViews(){

        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        // Initialize contacts
        tweets = new ArrayList<>();
        // Create adapter passing in the sample user data
        tweetsAdapter = new TweetsAdapter(this,tweets);
        // Attach the adapter to the recyclerview to populate items
        rvTweets.setAdapter(tweetsAdapter);
        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setHasFixedSize(true);

        scrollToTop();

        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                Intent i = new Intent(getApplicationContext(),DetailActivity.class);
                Tweet tweet = tweets.get(position);
                i.putExtra("tweet", Parcels.wrap(tweet));
                startActivity(i);

                //Toast.makeText(SearchActivity.this,"Hello",Toast.LENGTH_SHORT).show();
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(long page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                page = tweets.get(tweetsAdapter.getItemCount()-1).getUid();
                loadNextDataFromApi(page-1);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(long offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        populateTimeline(offset, 0);
    }

    // send a API request to get the JSON
    private void populateTimeline(long page, final int source) {
        client.getHomeTimeline(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //source = 1 means calling from swipeToRefresh
                // Remember to CLEAR OUT old items before appending in the new ones
                if(source == 1){ tweetsAdapter.clear(); }

                // Define Response class to correspond to the JSON response returned
                Type collectionType = new TypeToken<List<Tweet>>(){}.getType();
                tweets.addAll((Collection<? extends Tweet>) gson.fromJson(response.toString(),collectionType));
                //tweets = (List<Tweet>) new Gson().fromJson(response.toString(),collectionType);
                tweetsAdapter.notifyDataSetChanged();

                // call setRefreshing(false) to signal refresh has finished
                if(source == 1){
                    swipeContainer.setRefreshing(false);
                    scrollToTop();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("DEBUG", "Fetch timeline error: " + t.toString());
            }
        });
    }

    private void composeTweet() {

        fabCompose = (FloatingActionButton) findViewById(R.id.fabCompose);
        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser = new User();
                client.getLoginUserInfo(new TextHttpResponseHandler(
                ) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String response) {
                        loginUser = gson.fromJson(response,User.class);
                        showComposeDialog();
                        //Log.d("DEBUG",loginUser.getName());
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                        Log.d("DEBUG",error.toString());
                    }
                });
            }
        });
    }

    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment filterSearchDialogFragment = ComposeTweetDialogFragment.newInstance(loginUser,new Tweet());
        filterSearchDialogFragment.show(fm, "fragment_compose_tweet");
    }

    private void swipeToRefresh() {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(defaultMaxID,1);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


}
