package com.codepath.apps.simpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.adapters.TweetsAdapter;
import com.codepath.apps.simpletweets.activities.DetailActivity;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.utils.ItemClickSupport;
import com.codepath.apps.simpletweets.utils.TwitterApplication;
import com.codepath.apps.simpletweets.utils.TwitterClient;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ChaoJung on 2017/3/9.
 */

public abstract class TweetsListFragment extends Fragment {

    private EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;

    TweetsAdapter tweetsAdapter;
    RecyclerView rvTweets;

    List<Tweet> tweets;
    long defaultMaxID = 0;

    private TwitterClient client;

    //inflation login
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        setupViews(v);
        swipeToRefresh();

        return v;
    }

    //creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize contacts
        tweets = new ArrayList<>();
        // Create adapter passing in the sample user data
        tweetsAdapter = new TweetsAdapter(getActivity(),tweets);

        client = TwitterApplication.getRestClient(); //singleton client

    }

    public void setupViews(View v){

        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweets);
        // Attach the adapter to the recyclerview to populate items
        rvTweets.setAdapter(tweetsAdapter);

        // Set layout manager to position the items
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setHasFixedSize(true);

        scrollToTop();

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

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


        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                Intent i = new Intent(getContext(),DetailActivity.class);
                Tweet tweet = tweets.get(position);
                i.putExtra("tweet", Parcels.wrap(tweet));
                startActivity(i);

                //Toast.makeText(SearchActivity.this,"Hello",Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(long offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()
        populateTimeline(offset,0);
    }

    public void updateTimeline(Tweet tweet){
        tweets.add(0,tweet);
        tweetsAdapter.notifyItemInserted(0);
        scrollToTop();
    }

    public void swipeToRefresh() {
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline(defaultMaxID,1);
                // call setRefreshing(false) to signal refresh has finished
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

    public void scrollToTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) rvTweets
                .getLayoutManager();
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    public TweetsAdapter getTweetsAdapter(){
        return tweetsAdapter;
    }

    protected abstract void populateTimeline(long page, int source);
}
