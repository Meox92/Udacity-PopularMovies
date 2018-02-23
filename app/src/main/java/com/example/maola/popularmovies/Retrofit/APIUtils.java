package com.example.maola.popularmovies.Retrofit;

/**
 * Created by Maola on 18/02/2018.
 */

public class APIUtils {
    static final String BASE_URL = "http://api.themoviedb.org/3/";
//    static final String BASE_URL_POSTER = "http://image.tmdb.org/t/p/";


   // static final String API_KEY = "7da17e1be90a88c4688b556b907d7498";



    public static MovieAPI getResults(){
        return RetrofitClient.getClient(BASE_URL).create(MovieAPI.class);
    }


}
