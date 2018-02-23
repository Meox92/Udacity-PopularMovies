package com.example.maola.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maola.popularmovies.Models.Movie;
import com.example.maola.popularmovies.Retrofit.MovieAPI;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.detail_title)
    TextView detailTitle;
    @BindView(R.id.detail_poster_thumb)
    ImageView detailPosterThumb;
    @BindView(R.id.detail_release_date)
    TextView detailReleaseDate;
    @BindView(R.id.detail_vote_average)
    TextView detailVoteAverage;
    @BindView(R.id.detail_overview )
    TextView detailOverview;

    private Movie movieList;
    private String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

//    private String
//    private String release_date;
//    private String overview;
//    private double vote_average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView textView = (TextView) findViewById(R.id.detail_title);

        Intent i = getIntent();
        movieList = i.getParcelableExtra("movie");
        String a = movieList.getTitle();
        //Toast.makeText(this,a , Toast.LENGTH_LONG).show();
        detailTitle.setText(a);
        detailReleaseDate.setText(movieList.getRelease_date());
        detailVoteAverage.setText(getResources().getString(R.string.average_rate).concat(String.valueOf(movieList.getVote_average())));
        detailOverview.setText(movieList.getOverview());

        String posterPath = movieList.getPoster_path();
        Picasso.with(getApplicationContext())
                .load(BASE_POSTER_URL + posterPath)
                .into(detailPosterThumb);

    }

}
