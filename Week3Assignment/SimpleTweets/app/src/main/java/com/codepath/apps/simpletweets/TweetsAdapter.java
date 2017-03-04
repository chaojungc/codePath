package com.codepath.apps.simpletweets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.models.Tweet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by ChaoJung on 2017/3/3.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public TextView tvScreenName;
        public TextView tvTime;
        public ImageView ivUpload;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvTime = (TextView) itemView.findViewById(R.id.tvTimeStamp);
            ivUpload = (ImageView) itemView.findViewById(R.id.ivUpload);

        }
    }

    // Store a member variable for the contacts
    private List<Tweet> mTweets;
    // Store the context for easy access
    private Context mContext;

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Pass in the contact array into the constructor
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        mTweets = tweets;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Tweet tweet = mTweets.get(position);

//        viewHolder.getBinding().setVariable(com.codepath.apps.simpletweets.BR.tweet,tweet);
//        viewHolder.getBinding().executePendingBindings();

        // Set item views based on your views and data model
        ImageView ivProfileImage = viewHolder.ivProfileImage;
        ivProfileImage.setImageResource(0);

        ImageView ivUpload = viewHolder.ivUpload;
        ivUpload.setImageResource(0);

        TextView tvUserName = viewHolder.tvUserName;
        tvUserName.setText(tweet.getUser().getName());

        TextView tvScreenName = viewHolder.tvScreenName;
        tvScreenName.setText("@"+tweet.getUser().getScreenName());

        TextView tvTime = viewHolder.tvTime;
        tvTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

        TextView tvBody = viewHolder.tvBody;
        tvBody.setText(Html.fromHtml(tweet.getBody()).toString());

        Glide.with(mContext)
                .load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileImage);


        if(tweet.getEntities().getMedia() != null){
            Glide.with(mContext)
                    .load(tweet.getEntities().getMedia().get(0).getMediaUrl())
                    .into(ivUpload);
//            Log.d("DEBUG:IMAGE_URL = ",tweet.getEntities().getMedia().get(0).getUrl());
//            Log.d("DEBUG:TYPE = ",tweet.getEntities().getMedia().get(0).getType());
        }

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    //"Wed Mar 03 19:37:35 +0000 2010"
    public String getRelativeTimeAgo(String rawJsonDate) {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;

    }
}
