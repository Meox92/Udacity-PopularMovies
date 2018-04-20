package com.example.maola.popularmovies;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.maola.popularmovies.Adapter.MovieAdapter;
import com.example.maola.popularmovies.Data.MovieDBContract;
import com.example.maola.popularmovies.Models.Movie;
import com.example.maola.popularmovies.Models.Results;
import com.example.maola.popularmovies.Retrofit.APIUtils;
import com.example.maola.popularmovies.Retrofit.MovieAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {
    private MovieAPI mService;
    private List<Movie> moviesList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressDialog progressDialog;
    private static String LIST_SORT_BY = "sort_by";
    private String LIST_SORT_BY_VALUE;
    private static String POPULAR = "popular";
    private static String RATED = "rated";
    private List prova;
    private static final int MOVIE_LOADER = 22;
    private static final String SEARCH_MOVIE_URL_EXTRA = "query";


//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        savedInstanceState.getString(LIST_SORT_BY);
//        Log.i(LIST_SORT_BY, LIST_SORT_BY_VALUE);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mService = APIUtils.getResults();

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        GridLayoutManager manager = new GridLayoutManager(this , 2);
        mRecyclerView.setLayoutManager(manager);

        if (savedInstanceState != null) {
            String sort = savedInstanceState.getString(LIST_SORT_BY, "default");
            switch (sort){
                case "popular":
                    loadPopularResults();
                    break;
                case "rated":
                    loadTopRatedResults();
                    break;

                default:
                    loadPopularResults();
            }

            Log.i("OnCreate sort_by", savedInstanceState.getString(LIST_SORT_BY, "default"));
        }else{
            loadPopularResults();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LIST_SORT_BY, LIST_SORT_BY_VALUE);
        Log.i("OnCreate sort_by", outState.toString());

    }

    public void loadPopularResults() {
        LIST_SORT_BY_VALUE = POPULAR;

        progressDialog = ProgressDialog.show(MainActivity.this, "Loading in progress",
                "", true);
        mService.getPopularResults(BuildConfig.API_KEY).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {

                if(response.isSuccessful()) {
                    moviesList = response.body().getResults();
                    mAdapter = new MovieAdapter(moviesList);
                    mRecyclerView.setAdapter(mAdapter);

                }else {
                    int statusCode  = response.code();
                    Log.i("MainActivity", "error loading from API " + statusCode);
                    loadFavorite();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.i("MainActivity", "error loading from API");
                Toast.makeText(getApplicationContext(), "error loading from API", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }


    public void loadTopRatedResults() {
        LIST_SORT_BY_VALUE = RATED;
        progressDialog = ProgressDialog.show(MainActivity.this, "Loading in progress",
                "", true);
        mService.getTopRatedResults(BuildConfig.API_KEY).enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {

                if(response.isSuccessful()) {
                    moviesList = response.body().getResults();
                    mAdapter = new MovieAdapter(moviesList);
                    mRecyclerView.setAdapter(mAdapter);
                    progressDialog.dismiss();
                }else {
                    int statusCode  = response.code();
                    Log.i("MainActivity", "error loading from API " + statusCode);
                    Toast.makeText(getApplicationContext(), "Network error, check your connection", Toast.LENGTH_LONG).show();
                    loadFavorite();
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.i("MainActivity", "error loading from API");
                Toast.makeText(getApplicationContext(), "Network error, check your connection", Toast.LENGTH_LONG).show();
            }

        });
        //progressDialog.dismiss();
    }

    private Cursor loadFavorite(){
        Bundle queryBundle = new Bundle();
        // COMPLETED (20) Use putString with SEARCH_QUERY_URL_EXTRA as the key and the String value of the URL as the value
        queryBundle.putString(SEARCH_MOVIE_URL_EXTRA, "query");
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> movieLoader = loaderManager.getLoader(MOVIE_LOADER);
        if (movieLoader == null) {
            loaderManager.initLoader(MOVIE_LOADER, null, this);
        } else {
            loaderManager.restartLoader(MOVIE_LOADER, null, this);
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.top_rated) {
            loadTopRatedResults();
            return true;
        }
        if (id == R.id.popular) {
            loadPopularResults();
            return true;
        }
        if (id == R.id.favorite) {
            loadFavorite();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(this, "Clicked on " + clickedItemIndex, Toast.LENGTH_LONG).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mMovieData = null;
            @Override
            protected void onStartLoading(){
                progressDialog = ProgressDialog.show(MainActivity.this, "Loading in progress","Please wait...");

                if(mMovieData != null){
                    deliverResult(mMovieData);
                }else{
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieDBContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieDBContract.MovieEntry.COLUMN_MOVIE_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("on loadInBackground", e.toString());
                    return null;
                }
        }
    };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressDialog.dismiss();
        if (data != null && data.getCount() > 0) {
            moviesList.clear();

            if (data.moveToFirst()){
                do{

                  Movie movie = new Movie(data.getInt(1),
                          data.getString(2),
                          data.getString(3),
                          data.getDouble(4),
                          data.getInt(5),
                          data.getString(6),
                          data.getString(7),
                          data.getString(8),
                          true
                          );
                    moviesList.add(movie);

                }while(data.moveToNext());
            }
            data.close();
            mAdapter = new MovieAdapter(moviesList);
            mRecyclerView.setAdapter(mAdapter);
           // Log.i("LoadFinished data", loader + "" + data);

        } else {
            Log.i("LoadFinished", "no load data");
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
