package com.example.chaojung.nytimessearch;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Doc article = (Doc) Parcels.unwrap(getIntent().getParcelableExtra("article"));
        //Doc doc = (Doc) getIntent().getSerializableExtra("article");
        /*final WebView wvArticle = (WebView) findViewById(R.id.wvArticle);

        wvArticle.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //wvArticle.loadUrl(article.getWebUrl());
        */

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_black_24dp);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());

        int requestCode = 100;

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        // Map the bitmap, text, and pending intent to this icon
        // Set tint to be true so it matches the toolbar color
        builder.setActionButton(bitmap, "Share Link", pendingIntent, true);

        CustomTabsIntent customTabsIntent = builder.build();

//        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//        // set toolbar color and/or setting custom actions before invoking build()
//        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
//        CustomTabsIntent customTabsIntent = builder.build();

        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorAccent));
        builder.addDefaultShareMenuItem();


        // and launch the desired Url with CustomTabsIntent.launchUrl()
        customTabsIntent.launchUrl(this, Uri.parse(article.getWebUrl()));
    }

}
