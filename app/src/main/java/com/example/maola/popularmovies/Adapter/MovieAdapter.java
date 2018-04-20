package com.example.maola.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maola.popularmovies.Data.MovieDBContract;
import com.example.maola.popularmovies.DetailActivity;
import com.example.maola.popularmovies.Models.Movie;
import com.example.maola.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Maola on 18/02/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private List<Movie> moviesList;
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    private Movie movie;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public MovieAdapter(List<Movie> moviesList) {
        this.moviesList = moviesList;
        //mOnClickListener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.rv_item_poster, parent, false);
//        return new MyViewHolder(itemView);
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.rv_item_poster;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        //Set default color for smaller poster
        viewHolder.itemView.setBackgroundColor(Color.BLACK);
        //view.setOnClickListener(mOnClickListener);


        return viewHolder;
    }

//    @Override
//    public void onViewAttachedToWindow(MyViewHolder holder) {
//        holder.imageView.requestFocus();
//        super.onViewAttachedToWindow(holder);
//    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        movie = moviesList.get(position);
        //holder.title.setText(movie.getTitle());
        final Context context = holder.imageView.getContext();
        String posterPath = movie.getPoster_path();
        Picasso.with(context)
                .load(BASE_POSTER_URL + posterPath)
                .into(holder.imageView);

        // Check if the movie has been marked as favored
//        String [] column_movie_id = {String.valueOf(movie.getId())};
//        boolean favored = false;
//        Cursor data = context.getContentResolver().query(MovieDBContract.MovieEntry.CONTENT_URI,
//                null,
//                MovieDBContract.MovieEntry.COLUMN_MOVIE_ID +"=?",
//                column_movie_id,
//                null);

//        if(data.getCount() > 0){
//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Toast.makeText(context, moviesList.get(position) + " " + position, Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(context, DetailActivity.class);
//                    i.putExtra("movie", moviesList.get(position));
//                    i.putExtra("movie_favored", true);
//
//                    context.startActivity(i);
//
//                }
//            });
//        }else{
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, moviesList.get(position) + " " + position, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("movie", moviesList.get(position));
                    i.putExtra("movie_favored", false);

                    context.startActivity(i);

                }
            });
//        }

        holder.position = position;


    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        private int position;

        public MyViewHolder(View view) {
            super(view);
            //title = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.main_poster_iw);
        }

//        @Override
//        public void onClick(View view) {
//            int clickedPosition = getAdapterPosition();
//            //mOnClickListener.onListItemClick(clickedPosition);
//        }
    }
}