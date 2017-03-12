package com.codepath.apps.simpletweets.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.fragments.SearchTweetsFragment;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        String queryFromHashtag = getIntent().getStringExtra("query");

        SearchView svTweets = (SearchView) findViewById(R.id.svTweets);

        if(queryFromHashtag != null){
            svTweets.setQuery(queryFromHashtag,true);

            if(savedInstanceState == null){
                // Create the user timeline fragment
                SearchTweetsFragment fragmentSearchTweets = SearchTweetsFragment.newInstance(queryFromHashtag);
                // Display the user timeline fragment in the activity
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                // Insert the user timeline dynamic
                ft.replace(R.id.flContainer, fragmentSearchTweets);
                ft.commit(); // change the fragments
            }
        }
        else {
            svTweets.setQuery("",true);
            svTweets.setFocusable(true);
            svTweets.requestFocusFromTouch();
        }
        svTweets.setIconified(false);

        svTweets.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

//                Log.d("DEBUG",query);

                if(savedInstanceState == null){
                    // Create the user timeline fragment
                    SearchTweetsFragment fragmentSearchTweets = SearchTweetsFragment.newInstance(query);
                    // Display the user timeline fragment in the activity
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    // Insert the user timeline dynamic
                    ft.replace(R.id.flContainer, fragmentSearchTweets);
                    ft.commit(); // change the fragments
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

}
