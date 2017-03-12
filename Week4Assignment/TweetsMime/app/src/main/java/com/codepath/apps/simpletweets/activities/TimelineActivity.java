package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.ComposeTweetDialogFragment;
import com.codepath.apps.simpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.utils.TwitterApplication;
import com.codepath.apps.simpletweets.utils.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogListener {

   //private ActivityTimelineBinding binding;

    FloatingActionButton fabCompose;
    ViewPager vpPager;

    private TwitterClient client;
    User loginUser;

    @Override
    public void onFinishComposeDialog(final String update) {
        client.postTweet(update, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Gson gson = new GsonBuilder().create();
                Tweet tweet = gson.fromJson(responseBody,Tweet.class);
                HomeTimelineFragment fragmentHomeTimeline  = (HomeTimelineFragment) vpPager.getAdapter().instantiateItem(vpPager,0);
                fragmentHomeTimeline.updateTimeline(tweet);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
                Log.d("DEBUG",error.toString());
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApplication.getRestClient(); //singleton client

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // Get the viewPager
        vpPager = (ViewPager) findViewById(R.id.viewpager);
        // Set the viewPager adapter for the pager
        vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        // Find the sliding tabStrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //Attach the tabStrip to the viewPager
        tabStrip.setViewPager(vpPager);

        loginUser = new User();
        client.getLoginUserInfo(new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                Gson gson = new GsonBuilder().create();
                loginUser = gson.fromJson(response,User.class);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                Log.d("DEBUG",error.toString());
            }
        });

        composeTweet();

    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem mi){
        // launch the profile view
        String screenName = loginUser.getScreenName();
        Intent i = new Intent(this,ProfileActivity.class);
        i.putExtra("screenName", screenName);
        startActivity(i);
    }

    public void onSearchView(MenuItem mi){
        // launch the search view
        Intent i = new Intent(this,SearchActivity.class);
        startActivity(i);
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(mi);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                client.getSearchTweets(0, query, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        Gson gson = new GsonBuilder().create();
////                        loginUser = gson.fromJson(response,User.class);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//
//                    }
//                });
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
    }


    public class TweetsPagerAdapter extends FragmentPagerAdapter{

        private String tabTitles[] = {"Home","Mentions"};

        private int[] imageResId = {
                R.drawable.ic_home_black_24dp,
                R.drawable.ic_notifications_black_24dp
        };

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];

        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return new HomeTimelineFragment();
            }
            else if(position == 1){
                return new MentionsTimelineFragment();
            }
            else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

//        @Override
//        public int getPageIconResId(int position) {
//            return imageResId[position];
//        }
    }

    private void composeTweet() {

        fabCompose = (FloatingActionButton) findViewById(R.id.fabCompose);
        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showComposeDialog();
            }
        });
    }

    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment filterSearchDialogFragment = ComposeTweetDialogFragment.newInstance(loginUser,new Tweet());
        filterSearchDialogFragment.show(fm, "fragment_compose_tweet");
    }

}
