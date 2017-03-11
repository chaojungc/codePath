package com.codepath.apps.simpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.activities.DetailActivity;
import com.codepath.apps.simpletweets.activities.ProfileActivity;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.utils.PatternEditableBuilder;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

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

        private ImageView ivReply;
        private ImageView ivRetweet;
        private ImageView ivFavorite;
        private ImageView ivMessage;

        private TextView tvRetweetCount;
        private TextView tvFavoriteCount;

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

            ivReply = (ImageView) itemView.findViewById(R.id.ivReply);
            ivRetweet = (ImageView) itemView.findViewById(R.id.ivRetweet);
            ivFavorite = (ImageView) itemView.findViewById(R.id.ivFavorite);
            ivMessage = (ImageView) itemView.findViewById(R.id.ivMessage);
            tvRetweetCount = (TextView) itemView.findViewById(R.id.tvRetweetCount);
            tvFavoriteCount = (TextView) itemView.findViewById(R.id.tvFavoriteCount);

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
        final Tweet tweet = mTweets.get(position);

//        viewHolder.getBinding().setVariable(com.codepath.apps.simpletweets.BR.tweet,tweet);
//        viewHolder.getBinding().executePendingBindings();

        // Set item views based on your views and data model
        ImageView ivProfileImage = viewHolder.ivProfileImage;
        ivProfileImage.setImageResource(0);

        final String screenName = tweet.getUser().getScreenName();

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ProfileActivity.class);
                i.putExtra("screenName", screenName);
                getContext().startActivity(i);
            }
        });

        ImageView ivUpload = viewHolder.ivUpload;
        ivUpload.setImageResource(0);

        TextView tvUserName = viewHolder.tvUserName;
        tvUserName.setText(tweet.getUser().getName());

        TextView tvScreenName = viewHolder.tvScreenName;
        tvScreenName.setText("@"+tweet.getUser().getScreenName());
        // Style clickable spans based on pattern

        TextView tvTime = viewHolder.tvTime;
        tvTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

        TextView tvBody = viewHolder.tvBody;
        tvBody.setText(Html.fromHtml(tweet.getBody()).toString());
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\@(\\w+)"), Color.parseColor("#1da1f2"),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Intent i = new Intent(getContext(),ProfileActivity.class);
                                i.putExtra("screenName", text.substring(1));
                                getContext().startActivity(i);
                            }
                        }).into(tvBody);
        new PatternEditableBuilder().
                addPattern(Pattern.compile("\\#(\\w+)"), Color.parseColor("#1da1f2"),
                        new PatternEditableBuilder.SpannableClickedListener() {
                            @Override
                            public void onSpanClicked(String text) {
                                Toast.makeText(getContext(), "Clicked hashtag: " + text,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }).into(tvBody);

        Glide.with(mContext)
                .load(tweet.getUser().getProfileImageUrl())
                .into(ivProfileImage);

        if(tweet.getEntities().getMedia() != null){
            Glide.with(mContext)
                    .load(tweet.getEntities().getMedia().get(0).getMediaUrl())
                    .into(ivUpload);
        }

        // Actions to specific tweets
        ImageView ivReply = viewHolder.ivReply;
        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),DetailActivity.class);
                i.putExtra("tweet", Parcels.wrap(tweet));
                i.putExtra("replyFromTimeline",true);
                getContext().startActivity(i);
            }
        });

        final ImageView ivRetweet = viewHolder.ivRetweet;

        if(tweet.isRetweeted()){
            ivRetweet.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.MULTIPLY);
            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivRetweet.setColorFilter(Color.parseColor("#b9b9b9"), PorterDuff.Mode.MULTIPLY);
                }
            });
        }
        else {
            ivRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivRetweet.setColorFilter(Color.parseColor("#00FF00"), PorterDuff.Mode.MULTIPLY);
                }
            });
        }

        final ImageView ivFavorite = viewHolder.ivFavorite;
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFavorite.setColorFilter(Color.parseColor("#ff6961"), PorterDuff.Mode.MULTIPLY);
            }
        });

        TextView tvRetweetCount = viewHolder.tvRetweetCount;
        if(tweet.getRetweetCount() != 0){
            tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        }
        else{
            tvRetweetCount.setText("");
        }

        TextView tvFavoriteCount = viewHolder.tvFavoriteCount;
        Log.d("DEBUG-1234567",String.valueOf(tweet.getFavouritesCount()));
        if(tweet.getFavouritesCount() != 0){
            tvFavoriteCount.setText(String.valueOf(tweet.getFavouritesCount()));
        }
        else{
            tvFavoriteCount.setText("");
        }


    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public List getListItems() {
        return mTweets;
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
