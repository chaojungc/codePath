package com.example.chaojung.flickster;

import android.os.Bundle;

import com.example.chaojung.flickster.models.Trailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class YoutubeActivity extends YouTubeBaseActivity {

    ArrayList<Trailer> trailers;
    String trailerKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        trailers = new ArrayList<>();

        String movieID = getIntent().getStringExtra("movieID");
        String trailerUrl = "https://api.themoviedb.org/3/movie/"+movieID+"/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(trailerUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                JSONArray trailerJsonResult = null;
                try {
                    trailerJsonResult = response.getJSONArray("results");
                    trailers.addAll(Trailer.transJSONArray(trailerJsonResult));
                    trailerKey = trailers.get(0).getKey();

                    YouTubePlayerView youTubePlayerView =
                            (YouTubePlayerView) findViewById(R.id.youtubePlayer);

                    youTubePlayerView.initialize("AIzaSyAHQwbBgUDBqiTJxq_5N0qjlaLI9YjuprA",
                            new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                    YouTubePlayer youTubePlayer, boolean b) {

                                    // do any work here to cue video, play video, etc.
                                    youTubePlayer.loadVideo(trailerKey);
                                }
                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                    YouTubeInitializationResult youTubeInitializationResult) {

                                }
                            });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

            }
        });
        int code = getIntent().getIntExtra("code", 0);
    }
}
