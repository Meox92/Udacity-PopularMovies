package com.example.maola.popularmovies.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maola.popularmovies.DetailActivity;
import com.example.maola.popularmovies.Models.Movie;
import com.example.maola.popularmovies.Models.Trailer;
import com.example.maola.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Maola on 18/02/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private List<Trailer> trailerList;
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    private Trailer trailer;
    private String ytLinkKey = "";
    private String ytBaseThumbUrl = "https://img.youtube.com/vi/";
    private String ytFinalThumbUrl = "/0.jpg";

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public TrailerAdapter(List<Trailer> trailerList) {
        this.trailerList = trailerList;
        //mOnClickListener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.rv_item_poster, parent, false);
//        return new MyViewHolder(itemView);
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.rv_item_trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

//    @Override
//    public void onViewAttachedToWindow(MyViewHolder holder) {
//        holder.imageView.requestFocus();
//        super.onViewAttachedToWindow(holder);
//    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        trailer = trailerList.get(position);
        //holder.title.setText(movie.getTitle());
        final Context context = holder.imageView.getContext();
        final String ytLinkKey = trailer.getKey();
        String trailerTitle = trailer.getName();
        String linkThumb = ytBaseThumbUrl + ytLinkKey + ytFinalThumbUrl;

        holder.title.setText(trailerTitle);

        Picasso.with(context)
                .load(linkThumb)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, trailerList.get(position) + " " + position, Toast.LENGTH_LONG).show();

                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + ytLinkKey));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + ytLinkKey));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }


            }
        });

        holder.position = position;


    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        private int position;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.trailer_title);
            imageView = (ImageView) view.findViewById(R.id.trailer_imageView);
        }

//        @Override
//        public void onClick(View view) {
//            int clickedPosition = getAdapterPosition();
//            //mOnClickListener.onListItemClick(clickedPosition);
//        }
    }
}