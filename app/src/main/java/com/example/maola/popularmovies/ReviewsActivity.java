package com.example.maola.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.maola.popularmovies.Adapter.ReviewAdapter;
import com.example.maola.popularmovies.Models.Review;
import com.example.maola.popularmovies.Models.ReviewResults;
import com.example.maola.popularmovies.Retrofit.APIUtils;
import com.example.maola.popularmovies.Retrofit.MovieAPI;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {
    private static String MOVIE_ID = "MOVIE_ID";
    private RecyclerView recyclerView;
    private TextView textView;
    private List<Review> reviewList;
    private MovieAPI mService;
    private Integer movieID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        movieID = i.getIntExtra(MOVIE_ID, 0);
        mService = APIUtils.getResults();


        recyclerView = (RecyclerView)findViewById(R.id.reviews_rv);
        textView = (TextView) findViewById(R.id.review_no_review);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        getReviews();


    }

    private void getReviews(){
        mService.getReviews(movieID, BuildConfig.API_KEY).enqueue(new Callback<ReviewResults>() {

            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                reviewList = response.body().getResults();
                ReviewAdapter reviewAdapter = new ReviewAdapter(reviewList);
                recyclerView.setAdapter(reviewAdapter);
                textView.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {

            }

        });

    }

}
