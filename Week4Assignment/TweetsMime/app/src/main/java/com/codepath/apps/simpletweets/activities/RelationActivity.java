package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.adapters.RelationsAdapter;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.models.Users;
import com.codepath.apps.simpletweets.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.utils.ItemClickSupport;
import com.codepath.apps.simpletweets.utils.TwitterApplication;
import com.codepath.apps.simpletweets.utils.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RelationActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private TwitterClient client;

    RelationsAdapter relationsAdapter;
    RecyclerView rvRelations;

    List<User> users;
    Users userList;
    long defaultCursor = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation);

        client = TwitterApplication.getRestClient(); //singleton client

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        String screenName = getIntent().getStringExtra("screenName");
        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);

        setupViews(title,screenName);

        if(title.equals("Following")){
            populateFriendsList(defaultCursor,screenName, 0);
        }
        else {
            populateFollowerList(defaultCursor, screenName, 0);
        }

        swipeToRefresh(title,screenName);

    }

    private void setupViews(final String title, final String screenName) {

        rvRelations = (RecyclerView) findViewById(R.id.rvRelations);
        // Initialize contacts
        users = new ArrayList<>();
        // Create adapter passing in the sample user data
        relationsAdapter = new RelationsAdapter(this,users);
        // Attach the adapter to the recyclerview to populate items
        rvRelations.setAdapter(relationsAdapter);
        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvRelations.setLayoutManager(linearLayoutManager);
        rvRelations.setHasFixedSize(true);

        scrollToTop();

        ItemClickSupport.addTo(rvRelations).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                User user = users.get(position);
                i.putExtra("screenName", user.getScreenName());
                startActivity(i);

            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(long page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                page = userList.getNextCursor();
                Log.d("DEBUG-1234567",String.valueOf(userList.getNextCursor()));
                if(page > 0) {
                    Log.d("DEBUG-1234567","In LOOP "+String.valueOf(page));
                    if (title.equals("Following")) {
                        populateFriendsList(page, screenName, 0);
                    } else {
                        populateFollowerList(page, screenName, 0);
                    }
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        rvRelations.addOnScrollListener(scrollListener);

    }

    private void populateFollowerList(long cursor, String screenName, final int source) {

        client.getFollowerList(cursor, screenName, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                //source = 1 means calling from swipeToRefresh
                if(source == 1){ relationsAdapter.clear(); }

                userList = new Users();
                Gson gson = new GsonBuilder().create();
                userList = gson.fromJson(response,Users.class);
                users.addAll(userList.getUsers());
                relationsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                Log.d("DEBUG",error.toString());
            }
        });
    }

    private void populateFriendsList(long cursor,String screenName, final int source) {

        client.getFriendList(cursor, screenName, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                //source = 1 means calling from swipeToRefresh
                if(source == 1){ relationsAdapter.clear(); }

                userList = new Users();
                Gson gson = new GsonBuilder().create();
                userList = gson.fromJson(response,Users.class);
                users.addAll(userList.getUsers());
                relationsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                Log.d("DEBUG",error.toString());
            }
        });
    }

    private void swipeToRefresh(final String title, final String screenName) {
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                if(title.equals("Following")){
                    populateFriendsList(defaultCursor,screenName, 1);
                }
                else {
                    populateFollowerList(defaultCursor, screenName, 1);
                }

                swipeContainer.setRefreshing(false);
                scrollToTop();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void scrollToTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvRelations
                .getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }
}
