package com.example.chaojung.flickster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chaojung.flickster.DetailActivity;
import com.example.chaojung.flickster.R;
import com.example.chaojung.flickster.YoutubeActivity;
import com.example.chaojung.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by ChaoJung on 2017/2/14.
 */

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    // View lookup cache
    static class ViewHolder {

        @BindView(R.id.ivMovieImage) ImageView ivImage;
        @Nullable @BindView(R.id.tvTitle) TextView tvTitle;
        @Nullable @BindView(R.id.tvOverview) TextView tvOverview;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

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

        final Movie movie = getItem(position);
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

            viewHolder = new ViewHolder(convertView);

            /*viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(tvOverview);*/

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        }
        else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ivImage.setImageResource(0);
        final String trailerUrl = "https://api.themoviedb.org/3/movie/"+movie.getId()+"/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

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
                    Intent i = new Intent(getContext(), DetailActivity.class);
                    i.putExtra("trailerUrl", trailerUrl);
                    i.putExtra("originalTitle", movie.getOriginalTitle());
                    i.putExtra("overview", movie.getOverview());
                    i.putExtra("releaseDate", movie.getReleaseDate());
                    i.putExtra("voteAverage", movie.getVoteAverage());
                    // brings up the second activity
                    getContext().startActivity(i);
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
                    Intent i = new Intent(getContext(), YoutubeActivity.class);
                    i.putExtra("trailerUrl", trailerUrl);
                    // brings up the second activity
                    getContext().startActivity(i);
                    //gotoYoutubeActivity(position); //前往某個Activity(自行修改成要觸發的行為)
                }
            });

        }

        return convertView;
    }
}
