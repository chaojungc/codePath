package com.example.chaojung.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chaojung.flickster.R;
import com.example.chaojung.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.example.chaojung.flickster.R.id.tvOverview;

/**
 * Created by ChaoJung on 2017/2/14.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // View lookup cache
    private static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvOverview;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context,android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        if (Float.parseFloat(getItem(position).getVoteAverage()) >= 5.0f) {
            return 1;
        }
        else {
            return 0;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        ViewHolder viewHolder;
        int type = getItemViewType(position);
        int orientation = getContext().getResources().getConfiguration().orientation;

        if(convertView == null) {

            if(type == 0){ //not that popular movie

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
            }
            else{
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.popular_item_movie, parent, false);

            }

            viewHolder = new ViewHolder();

            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(tvOverview);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }
        else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);

        if (type == 0) { //not that popular movie

            viewHolder.tvTitle.setText(movie.getOriginalTitle());
            viewHolder.tvOverview.setText(movie.getOverview());
            Picasso.with(getContext()).load(movie.getPosterPath()).transform(new RoundedCornersTransformation(10, 10))
                    .resize(480,0)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.ivImage);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "detailPage",Toast.LENGTH_SHORT).show();
                    //gotoDetailActivity(position); //前往某個Activity(自行修改成要觸發的行為)
                }
            });

        } else {

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                viewHolder.tvTitle.setText(movie.getOriginalTitle());
                viewHolder.tvOverview.setText(movie.getOverview());
            }
            Picasso.with(getContext()).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(10, 10))
                    .resize(1000,0)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.ivImage);
            viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "youtube",Toast.LENGTH_SHORT).show();
                    //gotoYoutubeActivity(position); //前往某個Activity(自行修改成要觸發的行為)
                }
            });

        }

        return convertView;
    }
}
