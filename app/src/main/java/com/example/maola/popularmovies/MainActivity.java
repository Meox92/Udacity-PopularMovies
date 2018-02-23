package com.example.maola.popularmovies;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.maola.popularmovies.Adapter.MovieAdapter;
import com.example.maola.popularmovies.Models.Movie;
import com.example.maola.popularmovies.Models.Results;
import com.example.maola.popularmovies.Retrofit.APIUtils;
import com.example.maola.popularmovies.Retrofit.MovieAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {
    private MovieAPI mService;
    private List<Movie> moviesList;
    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private ProgressDialog progressDialog;

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


        loadPopularResults();
    }

    public void loadPopularResults() {
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
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.i("MainActivity", "error loading from API");
                Toast.makeText(getApplicationContext(), "Network error, check your connection", Toast.LENGTH_LONG).show();
            }

        });
        progressDialog.dismiss();


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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(this, "Clicked on " + clickedItemIndex, Toast.LENGTH_LONG).show();
    }
}
