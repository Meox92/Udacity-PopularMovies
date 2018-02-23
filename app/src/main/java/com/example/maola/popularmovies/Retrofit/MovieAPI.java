package com.example.maola.popularmovies.Retrofit;

import com.example.maola.popularmovies.Models.Movie;
import com.example.maola.popularmovies.Models.Results;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Maola on 17/02/2018.
 */

public interface MovieAPI {

    static final String BASE_URL_POSTER = "http://image.tmdb.org/t/p/";

    @GET("movie/popular")
    Call<Results> getPopularResults(@Query("api_key") String API_KEY);

    @GET("movie/top_rated")
    Call<Results> getTopRatedResults(@Query("api_key") String API_KEY);

    @GET("movie/popular")
    Call<String> getPoster();
}
