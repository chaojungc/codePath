package com.codepath.apps.simpletweets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.User;

import java.util.List;

/**
 * Created by ChaoJung on 2017/3/11.
 */

public class RelationsAdapter extends RecyclerView.Adapter<RelationsAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvScreenName;
        public TextView tvDescription;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvScreenName = (TextView) itemView.findViewById(R.id.tvScreenName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);

        }
    }

    // Store a member variable for the contacts
    private List<User> mUsers;
    // Store the context for easy access
    private Context mContext;

    // Clean all elements of the recycler
    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    // Pass in the contact array into the constructor
    public RelationsAdapter(Context context, List<User> users) {
        mUsers = users;
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
        View contactView = inflater.inflate(R.layout.item_user, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Get the data model based on position
        User user = mUsers.get(position);

//        viewHolder.getBinding().setVariable(com.codepath.apps.simpletweets.BR.tweet,tweet);
//        viewHolder.getBinding().executePendingBindings();

        // Set item views based on your views and data model
        ImageView ivProfileImage = viewHolder.ivProfileImage;
        ivProfileImage.setImageResource(0);

        TextView tvUserName = viewHolder.tvUserName;
        tvUserName.setText(user.getName());

        TextView tvScreenName = viewHolder.tvScreenName;
        tvScreenName.setText("@"+user.getScreenName());

        TextView tvDescription = viewHolder.tvDescription;
        tvDescription.setText(user.getDescription());

        Glide.with(mContext)
                .load(user.getProfileImageUrl())
                .into(ivProfileImage);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
