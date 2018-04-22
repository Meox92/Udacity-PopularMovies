package com.example.maola.popularmovies.Retrofit;

/**
 * Created by Maola on 18/02/2018.
 */

public class APIUtils {
    static final String BASE_URL = "http://api.themoviedb.org/3/";
//    static final String BASE_URL_POSTER = "http://image.tmdb.org/t/p/";


    public static MovieAPI getResults(){
        return RetrofitClient.getClient(BASE_URL).create(MovieAPI.class);
    }


}
