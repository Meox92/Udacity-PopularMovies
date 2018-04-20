package com.example.maola.popularmovies.Retrofit;

import com.example.maola.popularmovies.Models.Results;
import com.example.maola.popularmovies.Models.ResultsTrailer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Maola on 17/02/2018.
 */

public interface MovieAPI {

    String BASE_URL_POSTER = "http://image.tmdb.org/t/p/";

    @GET("movie/popular")
    Call<Results> getPopularResults(@Query("api_key") String API_KEY);

    @GET("movie/top_rated")
    Call<Results> getTopRatedResults(@Query("api_key") String API_KEY);

    @GET("movie/popular")
    Call<String> getPoster();

    @GET("movie/{id}/videos")
    Call<ResultsTrailer> getTrailers (@Path("id") int id, @Query("api_key") String API_KEY);

    @GET("movie/{id}/reviews")
    Call<Results> getReviews (@Path("id") int id, @Query("api_key") String API_KEY);
}
