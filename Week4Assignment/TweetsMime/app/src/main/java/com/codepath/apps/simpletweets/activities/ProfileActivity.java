package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.utils.PatternEditableBuilder;
import com.codepath.apps.simpletweets.utils.TwitterApplication;
import com.codepath.apps.simpletweets.utils.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;


public class ProfileActivity extends AppCompatActivity {

    User currentUser;
    private TwitterClient client;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        client = TwitterApplication.getRestClient(); //singleton client

        String screenName = getIntent().getStringExtra("screenName");

        client.getUserInfo(screenName, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {

                Gson gson = new GsonBuilder().create();
                currentUser = gson.fromJson(response,User.class);
                //getSupportActionBar().setTitle("@"+currentUser.getScreenName());

                populateProfileHeader(currentUser);

                if(savedInstanceState == null){
                    // Create the user timeline fragment
                    UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(currentUser.getScreenName());
                    // Display the user timeline fragment in the activity
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    // Insert the user timeline dynamic
                    ft.replace(R.id.flContainer, fragmentUserTimeline);
                    ft.commit(); // change the fragments
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable error) {
                Log.d("DEBUG",error.toString());
            }
        });

    }

    private void populateProfileHeader(User currentUser) {

        ImageView ivBanner = (ImageView) findViewById(R.id.ivBanner);
        ivBanner.setImageResource(0);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageResource(0);

        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        TextView tvfollowing = (TextView) findViewById(R.id.tvfollowing);
        TextView tvfollower = (TextView) findViewById(R.id.tvfollower);

        tvUserName.setText(currentUser.getName());
        tvScreenName.setText("@" + currentUser.getScreenName());
        tvDescription.setText(currentUser.getDescription());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.parseColor("#1da1f2"),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                                i.putExtra("screenName", text.substring(1));
                                startActivity(i);
                            }
                        }).into(tvDescription);
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), Color.parseColor("#1da1f2"),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getApplicationContext(), "Clicked hashtag: " + text,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }).into(tvDescription);

        tvfollowing.setText(currentUser.getFollowingCount() + " Following");
        final String screenName = currentUser.getScreenName();
        tvfollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RelationActivity.class);
                i.putExtra("title", "Following");
                i.putExtra("screenName", screenName);
                startActivity(i);
            }
        });

        tvfollower.setText(currentUser.getFollowerCount() + " Follower");
        tvfollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RelationActivity.class);
                i.putExtra("title", "Follower");
                i.putExtra("screenName", screenName);
                startActivity(i);
            }
        });

        Glide.with(this)
                .load(currentUser.getProfileBannerUrl())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivBanner);

        Glide.with(this)
                .load(currentUser.getProfileImageUrl())
                .into(ivProfileImage);


    }


}
