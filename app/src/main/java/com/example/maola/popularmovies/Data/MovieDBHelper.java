package com.example.maola.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Maola on 03/03/2018.
 */

public class MovieDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DB_VERSION = 1;

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + MovieDBContract.MovieEntry.TABLE_NAME + " (" +
                MovieDBContract.MovieEntry._ID                + " INTEGER PRIMARY KEY, " +
                MovieDBContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL," +
                MovieDBContract.MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                MovieDBContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT," +
                MovieDBContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " REAL," +
                MovieDBContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT + " INTEGER," +
                MovieDBContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT," +
                MovieDBContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT," +
                MovieDBContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT," +
                MovieDBContract.MovieEntry.COLUMN_MOVIE_FAVORED + " INTEGER NOT NULL DEFAULT 0," +
                "UNIQUE (" + MovieDBContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE)";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MovieDBContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
