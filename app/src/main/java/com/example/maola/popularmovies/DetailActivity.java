package com.example.maola.popularmovies;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maola.popularmovies.Adapter.TrailerAdapter;
import com.example.maola.popularmovies.Data.MovieDBContract;
import com.example.maola.popularmovies.Models.Movie;
import com.example.maola.popularmovies.Models.ResultsTrailer;
import com.example.maola.popularmovies.Models.Trailer;
import com.example.maola.popularmovies.Retrofit.APIUtils;
import com.example.maola.popularmovies.Retrofit.MovieAPI;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
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
    @BindView(R.id.detail_overview)
    TextView detailOverview;
    @BindView(R.id.detail_trailer_lv)
    RecyclerView detailTrailerRv;
    @BindView(R.id.detail_favorite)
    Button detailFavorite;
    @BindView(R.id.detail_backdrop)
    ImageView detailBackdrop;
//    @BindView(R.id.detail_trailer_lv)
//    ListView detailTrailerLv;

    private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";
    private final String BASE_BACKDROP_URL = "http://image.tmdb.org/t/p/w342";

    private Movie movie;
    private MovieAPI mService;
    private List<Trailer> trailerList;
    private ArrayAdapter adapter;
    private TrailerAdapter mAdapter;
    private boolean favored;
    private static final int FAVORED_LOADER = 22;
    private static final int DELETE_LOADER = 23;
    private LoaderManager loaderManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView textView = (TextView) findViewById(R.id.detail_title);

        Intent i = getIntent();
        movie = i.getParcelableExtra("movie");
        //favored = i.getBooleanExtra("movie_favored", false);
        String a = movie.getTitle();
        detailTitle.setText(a);

        detailReleaseDate.setText(movie.getRelease_date());
        detailVoteAverage.setText(getResources().getString(R.string.average_rate).concat(String.valueOf(movie.getVote_average())) + "/10");
        detailOverview.setText(movie.getOverview());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        detailTrailerRv.setLayoutManager(layoutManager);
        //favored = movie.getFavored();

        loaderManager = getSupportLoaderManager();

        checkIfFavored();


        detailFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

        mService = APIUtils.getResults();

        setPosterThumb();
        loadTrailers();


    }

    private void checkIfFavored(){
        Loader<Cursor> movieLoader = loaderManager.getLoader(FAVORED_LOADER);
        if (movieLoader == null) {
            loaderManager.initLoader(FAVORED_LOADER, null, this);
        } else {
            loaderManager.restartLoader(FAVORED_LOADER, null, this);
        }
    }

    private void toggleFavorite() {
        favored = movie.setFavored(!favored);

        if(favored){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVote_count());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVote_average());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_FAVORED, true);
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPoster_path());
        contentValues.put(MovieDBContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getBackdrop_path());

        //Insert new movie data via a ContentResolver
        Uri uri = getContentResolver().insert(MovieDBContract.MovieEntry.CONTENT_URI, contentValues);
        if(uri != null){
            //Display the URI that's returned
            Toast.makeText(this, "Added to favorite" + uri, Toast.LENGTH_SHORT).show();
        }
        }else{
           deleteMoviefromDB();
        }
        checkIfFavored();
    }

    private void deleteMoviefromDB(){
        Loader<Cursor> movieLoader = loaderManager.getLoader(DELETE_LOADER);
        if (movieLoader == null) {
            loaderManager.initLoader(DELETE_LOADER, null, this);
        } else {
            loaderManager.restartLoader(DELETE_LOADER, null, this);
        }

//        String [] column_movie_id = {String.valueOf(movie.getId())};
//        // Query the movie and delete it
//        Cursor data = getContentResolver().query(MovieDBContract.MovieEntry.CONTENT_URI,
//                null,
//                MovieDBContract.MovieEntry.COLUMN_MOVIE_ID +"=?",
//                column_movie_id,
//                null);
//        int id = -1;
//        String title = "";
//        if (data.moveToFirst()){
//            id = data.getInt(0);
//            title = data.getString(2);
//        }
//        data.close();
//        if(id != -1){
//            Uri uri = MovieDBContract.MovieEntry.CONTENT_URI;
//            uri = uri.buildUpon().appendPath(Integer.toString(id)).build();
//
//            int deleted = getContentResolver().delete(uri,
//                    MovieDBContract.MovieEntry.COLUMN_MOVIE_ID +"=?",
//                    column_movie_id);
//
//            Toast.makeText(this,
//                    "Deleted " + title + " from favorite",
//                    Toast.LENGTH_SHORT).show();
//
//        }
    }

    private void setPosterThumb() {
        String posterPath = movie.getPoster_path();
        Picasso.with(getApplicationContext())
                .load(BASE_POSTER_URL + posterPath)
                .into(detailPosterThumb);

        Picasso.with(getApplicationContext())
                .load(BASE_BACKDROP_URL + movie.getBackdrop_path())
                .into(detailBackdrop);
    }

    private void loadTrailers() {
        mService.getTrailers(movie.getId(), BuildConfig.API_KEY).enqueue(new Callback<ResultsTrailer>() {
            @Override
            public void onResponse(Call<ResultsTrailer> call, Response<ResultsTrailer> response) {
                trailerList = response.body().getResults();
                mAdapter = new TrailerAdapter(trailerList);
                detailTrailerRv.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<ResultsTrailer> call, Throwable t) {

            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mMovieData = null;
            @Override
            protected void onStartLoading(){
                if(mMovieData != null){
                    deliverResult(mMovieData);
                }else{
                    forceLoad();
                }
            }


            @Override
            public Cursor loadInBackground() {
                String [] column_movie_id = {String.valueOf(movie.getId())};

                Cursor data = getApplicationContext().getContentResolver().query(MovieDBContract.MovieEntry.CONTENT_URI,
                        null,
                        MovieDBContract.MovieEntry.COLUMN_MOVIE_ID +"=?",
                        column_movie_id,
                        null);


                return data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int loaderId = loader.getId();

         switch (loaderId){
             case FAVORED_LOADER:
                 if (data != null && data.getCount() > 0){
                     favored = true;
                     detailFavorite.setBackgroundResource(R.drawable.star_24_actived);
                     Toast.makeText(this, "LoadFinished " + favored + " data not null", Toast.LENGTH_SHORT ).show();
                 }else{
                     favored = false;
                     detailFavorite.setBackgroundResource(R.drawable.star_24_deactived);
                     Toast.makeText(this, "LoadFinished " + favored, Toast.LENGTH_SHORT ).show();
                 }
                 break;

             case DELETE_LOADER:
                String [] column_movie_id = {String.valueOf(movie.getId())};
                int id = -1;
                String title = "";
                if (data.moveToFirst()){
                    id = data.getInt(0);
                    title = data.getString(2);
                }
                data.close();
                if(id != -1){
                    Uri uri = MovieDBContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(Integer.toString(id)).build();

                    int deleted = getContentResolver().delete(uri,
                            MovieDBContract.MovieEntry.COLUMN_MOVIE_ID +"=?",
                            column_movie_id);

                    Toast.makeText(this,
                            "Deleted " + title + " from favorite",
                            Toast.LENGTH_SHORT).show();
                }
                checkIfFavored();
                break;


             default:
                 Log.i("Detail loader", "Error on loading data");
        }

        }
//        if(loaderId == FAVORED_LOADER){
//        if (data != null && data.getCount() > 0){
//            favored = true;
//            detailFavorite.setBackgroundResource(R.drawable.star_24_actived);
//            Toast.makeText(this, "LoadFinished " + favored + " data not null", Toast.LENGTH_SHORT ).show();
//        }else{
//            favored = false;
//            detailFavorite.setBackgroundResource(R.drawable.star_24_deactived);
//            Toast.makeText(this, "LoadFinished " + favored, Toast.LENGTH_SHORT ).show();
//        }}else if(loaderId == DELETE_LOADER ){
//            String [] column_movie_id = {String.valueOf(movie.getId())};
//            int id = -1;
//            String title = "";
//            if (data.moveToFirst()){
//                id = data.getInt(0);
//                title = data.getString(2);
//            }
//            data.close();
//            if(id != -1){
//                Uri uri = MovieDBContract.MovieEntry.CONTENT_URI;
//                uri = uri.buildUpon().appendPath(Integer.toString(id)).build();
//
//                int deleted = getContentResolver().delete(uri,
//                        MovieDBContract.MovieEntry.COLUMN_MOVIE_ID +"=?",
//                        column_movie_id);
//
//                Toast.makeText(this,
//                        "Deleted " + title + " from favorite",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        }
//    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
