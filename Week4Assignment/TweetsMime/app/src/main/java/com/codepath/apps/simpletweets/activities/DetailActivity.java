package com.codepath.apps.simpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.ComposeTweetDialogFragment;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.codepath.apps.simpletweets.utils.PatternEditableBuilder;
import com.codepath.apps.simpletweets.utils.TwitterApplication;
import com.codepath.apps.simpletweets.utils.TwitterClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.TextHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity implements ComposeTweetDialogFragment.ComposeTweetDialogListener {

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

    EditText etTweetContent;
    TextView tvCountText;
    Button btnTweet;

    User loginUser;
    Tweet tweet;

    @Override
    public void onFinishComposeDialog(String update) {
        client.postTweet(update, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseBody) {
                Intent i = new Intent(getApplicationContext(),TimelineActivity.class);
                startActivity(i);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseBody, Throwable error) {
            }
        });
    }

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
        boolean replyFromTimeline = getIntent().getBooleanExtra("replyFromTimeline",false);

        if(!replyFromTimeline){
            RelativeLayout rlReply = (RelativeLayout) findViewById(R.id.rlReply);
            rlReply.setFocusableInTouchMode(true);
        }
        else{
            //scroll to bottom if intent is to reply tweet
            final NestedScrollView scrollview = ((NestedScrollView) findViewById(R.id.nsvDetail));
            scrollview.post(new Runnable() {
                @Override
                public void run() {
                    scrollview.scrollTo(0, scrollview.getHeight());
                }
            });
        }

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        tvBody = (TextView) findViewById(R.id.tvBody);
        ivUpload = (ImageView) findViewById(R.id.ivUpload);
        tvTime = (TextView) findViewById(R.id.tvTimeStamp);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        ibReply = (ImageButton) findViewById(R.id.ibReply);
        etTweetContent = (EditText) findViewById(R.id.etTweetContent);
        tvCountText = (TextView)findViewById(R.id.tvCountText);
        btnTweet = (Button) findViewById(R.id.btnTweet);

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText("@"+tweet.getUser().getScreenName());
        tvBody.setText(Html.fromHtml(tweet.getBody()).toString());
        //tvBody.setText(tweet.getBody());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.parseColor("#1da1f2"),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                                i.putExtra("screenName", text.substring(1));
                                startActivity(i);
                            }
                        }).into(tvBody);
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), Color.parseColor("#1da1f2"),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getApplicationContext(), "Clicked hashtag: " + text,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }).into(tvBody);

        ivUpload.setImageResource(0);
        if(tweet.getEntities().getMedia() != null){
            Glide.with(this)
                    .load(tweet.getEntities().getMedia().get(0).getMediaUrl())
                    .into(ivUpload);
        }

        tvTime.setText(formatTimestamp(tweet.getCreatedAt()));
        tvInfo.setText(String.valueOf(tweet.getRetweetCount()) + " RETWEETS " + String.valueOf(tweet.getFavouritesCount()) + " FAVORITES");

        ivProfileImage.setImageResource(0);
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        final String screenName = tweet.getUser().getScreenName();

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                i.putExtra("screenName", screenName);
                startActivity(i);
            }
        });

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

        if(tweet.getBody() == null){
            etTweetContent.setText("");
        }
        else{
            etTweetContent.setText("@"+tweet.getUser().getScreenName());
        }

        etTweetContent.addTextChangedListener(mTextEditorWatcher);
        etTweetContent.setSelection(etTweetContent.getText().length());

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = etTweetContent.getText().toString();
                onFinishComposeDialog(status);
            }
        });

    }

    private void showComposeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ComposeTweetDialogFragment filterSearchDialogFragment = ComposeTweetDialogFragment.newInstance(loginUser,tweet);
        filterSearchDialogFragment.show(fm, "fragment_compose_tweet");
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCountText.setText(String.valueOf(140 - s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

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
