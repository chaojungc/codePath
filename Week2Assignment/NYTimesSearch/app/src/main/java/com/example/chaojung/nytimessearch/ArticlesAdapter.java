package com.example.chaojung.nytimessearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by ChaoJung on 2017/2/20.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivImage;
        public TextView tvTitle;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

        }
    }

    // Store a member variable for the contacts
    //private List<Doc> mArticles;
    private List<Doc> mArticles;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public ArticlesAdapter(Context context, List<Doc> articles) {
        mArticles = articles;
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
        View contactView = inflater.inflate(R.layout.item_article_result, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Doc article = mArticles.get(position);

        // Set item views based on your views and data model
        ImageView imageView = viewHolder.ivImage;
        imageView.setImageResource(0);

        TextView textView = viewHolder.tvTitle;
        textView.setText(article.getHeadline().getMain());

        if(article.getMultimedia().size() != 0){
            Glide.with(mContext)
                    .load(article.getMultimedia().get(0).getUrl())
                    .placeholder(R.drawable.ic_more_horiz_black_24dp)
                    .into(imageView);
        }

        Log.d("DEBUG",article.getWebUrl());
        //Toast.makeText(mContext,article.getHeadline().getMain(),Toast.LENGTH_SHORT).show();

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mArticles.size();
    }


}
