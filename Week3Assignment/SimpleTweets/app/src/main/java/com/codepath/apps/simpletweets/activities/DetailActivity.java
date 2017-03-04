package com.codepath.apps.simpletweets.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.ComposeTweetDialogFragment;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.utils.TwitterApplication;
import com.codepath.apps.simpletweets.utils.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {

    Gson gson = new GsonBuilder().create();

    public TwitterClient client;

    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvScreenName;
    TextView tvBody;
    TextView tvTime;
    TextView tvInfo;
    ImageView ivUpload;

    ImageButton ibReply;

    User loginUser;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = TwitterApplication.getRestClient(); //singleton client

        setupView();

    }

    private void setupView() {

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        ivUpload = (ImageView) findViewById(R.id.ivUpload);
        tvTime = (TextView) findViewById(R.id.tvTimeStamp);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        ibReply = (ImageButton) findViewById(R.id.ibReply);

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText("@"+tweet.getUser().getScreenName());
        tvBody.setText(Html.fromHtml(tweet.getBody()).toString());
        //tvBody.setText(tweet.getBody());

        ivUpload.setImageResource(0);
        if(tweet.getEntities().getMedia() != null){
            Glide.with(this)
                    .load(tweet.getEntities().getMedia().get(0).getMediaUrl())
                    .into(ivUpload);
        }

        tvTime.setText(formatTimestamp(tweet.getCreatedAt()));
        tvInfo.setText(String.valueOf(tweet.getRetweetCount()) + " RETWEETS " + String.valueOf(tweet.getFavouritesCount()) + " FAVORITES");

        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        ibReply.setOnClickListener(new View.OnClickListener() {
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
        ComposeTweetDialogFragment filterSearchDialogFragment = ComposeTweetDialogFragment.newInstance(loginUser,tweet);
        filterSearchDialogFragment.show(fm, "fragment_compose_tweet");
    }

    public String formatTimestamp(String rawJsonDate) {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        String newFormat = "yyyy/MM/dd HH:mm";
        String result = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(twitterFormat);
            Date origin = sdf.parse(rawJsonDate);
            SimpleDateFormat df = new SimpleDateFormat(newFormat);
            result = df.format(origin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;

    }
}
