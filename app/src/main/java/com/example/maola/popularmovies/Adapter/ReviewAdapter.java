package com.example.maola.popularmovies.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maola.popularmovies.Models.Review;
import com.example.maola.popularmovies.Models.Trailer;
import com.example.maola.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Maola on 18/02/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private List<Review> reviewList;
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    private Review review;
    private String ytLinkKey = "";
    private String ytBaseThumbUrl = "https://img.youtube.com/vi/";
    private String ytFinalThumbUrl = "/0.jpg";

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.rv_item_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        review = reviewList.get(position);
        holder.author.setText(review.getAuthor());
        holder.content.setText(review.getContent());
        holder.position = position;


    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView author;
        public TextView content;
        private int position;

        public MyViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.review_author_tv);
            content = (TextView) view.findViewById(R.id.review_comment_tv);
        }

    }
}