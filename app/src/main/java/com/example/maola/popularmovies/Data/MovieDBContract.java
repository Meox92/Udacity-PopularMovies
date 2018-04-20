package com.example.maola.popularmovies.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Maola on 03/03/2018.
 */

public class MovieDBContract {
    /*The authority, which is how your code knows which Content Provider to access*/
    public static final String AUTHORITY = "com.example.maola.popularmovies";
    // content:// + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    //path for movie directory
    public static final String PATH_MOVIE = "movie";

    //empty constructor
    private MovieDBContract(){}

    /*Inner class that implements the BaseColumn interface
     */
    public static final class MovieEntry implements BaseColumns{
        //MovieEntry content URI = base content URI + path
        // content://com.example.maola.popularmovies/movie
        public final static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();


        public static String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_OVERVIEW = "movie_overview";
        public static final String COLUMN_MOVIE_VOTE_COUNT = "movie_vote_count";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movie_vote_average";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_MOVIE_FAVORED = "movie_favored";
        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_poster_path";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "movie_backdrop_path";
    }
}
