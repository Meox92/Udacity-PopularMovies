package com.example.maola.popularmovies.Data.ContentProvider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.maola.popularmovies.Data.MovieDBContract;
import com.example.maola.popularmovies.Data.MovieDBHelper;

/**
 * Created by Maola on 03/03/2018.
 */

public class MovieContentProvider extends ContentProvider {
    private MovieDBHelper mMovieDBHelper;

    //Integer constant for the directory of movies(for convention use round numbers like
    // 100, 200, 300 etc for dirs )
    private static final int MOVIES = 100;
    //Integer constant for the single item
    private static final int MOVIE_WITH_ID = 101;
    //static variable for Uri matcher
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //Helper method to build URI
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //directory
        uriMatcher.addURI(MovieDBContract.AUTHORITY, MovieDBContract.PATH_MOVIE, MOVIES);
        //single item
        uriMatcher.addURI(MovieDBContract.AUTHORITY, MovieDBContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDBHelper = new MovieDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //Get access to the db
        final SQLiteDatabase db = mMovieDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor retCursor;
        switch (match){
            case MOVIES:
                retCursor = db.query(MovieDBContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
//            case MOVIE_WITH_ID:
//                // using selection and selectionArgs
//                String id = uri.getPathSegments().get(1);
//                String mSelection = "movie_id=?";
//                String [] mSelectionArgs = new String[] {id};
//
//                retCursor = db.query(MovieDBContract.MovieEntry.TABLE_NAME,
//                        projection,
//                        mSelection,
//                        mSelectionArgs,
//                        null,
//                        null,
//                        sortOrder);
//
//                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIES:
                long id = db.insert(MovieDBContract.MovieEntry.TABLE_NAME, null, values);
                if(id > 0){
                    //success
                    returnUri = ContentUris.withAppendedId(MovieDBContract.MovieEntry.CONTENT_URI, id);

                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI" + uri);
        }
        //Notify the resolver if the uri has been changed and update the data
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Cursor retCursor = db.query(MovieDBContract.MovieEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                MovieDBContract.MovieEntry._ID);

        int deleted = db.delete(MovieDBContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
        //Notify the resolver if the uri has been changed and update the data
        getContext().getContentResolver().notifyChange(uri, null);

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
