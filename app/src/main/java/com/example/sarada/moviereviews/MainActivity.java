package com.example.sarada.moviereviews;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarada.moviereviews.data.FavoriteContract;
import com.example.sarada.moviereviews.models.MovieApiResponse;
import com.example.sarada.moviereviews.models.MovieDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.check) TextView check;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    private List<MovieDetails> movieList;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initViews();

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener(){
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(MainActivity.this,"Movies Refreshed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Activity getActivity(){
        Context context = this;
        while(context instanceof ContextWrapper){
            if(context instanceof Activity)
                return (Activity) context;
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private void initViews() {
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this,movieList);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        else
        {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,4);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener(){
            @Override
            public void onRefresh() {
                initViews();
                Toast.makeText(MainActivity.this,"Movies Refreshed",Toast.LENGTH_SHORT).show();
            }
        });
        checkSortOrder();
    }

    private void initViews2(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        movieAdapter.notifyDataSetChanged();
        getAllFavorite();

    }

    private void loadJSON(){
            try{
                if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please obtain API key from themoviedb.org",Toast.LENGTH_SHORT).show();
                    return;
                }

                RetrofitMake Client = new RetrofitMake();
                RetrofitQuery retrofitQuery = RetrofitMake.getClient().create(RetrofitQuery.class);
                Call<MovieApiResponse> call = retrofitQuery.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
                call.enqueue(new Callback<MovieApiResponse>(){
                    @Override
                    public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response){
                        List<MovieDetails> movies = response.body().getResults();
                        recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                        recyclerView.smoothScrollToPosition(0);
                        if(swipeRefreshLayout.isRefreshing())
                        {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieApiResponse> call, Throwable t){
                        Log.d("Error",t.getMessage());
                        check.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this,"Error Fetching popular movies!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            catch(Exception e){
                Log.d("Error",e.getMessage());
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
            }
    }

    private void loadJSON1(){

        Toast.makeText(getApplicationContext(),"App started.",Toast.LENGTH_SHORT).show();
        try{
            if(BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please obtain API key from themoviedb.org",Toast.LENGTH_SHORT).show();
                return;
            }

            RetrofitMake Client = new RetrofitMake();
            RetrofitQuery retrofitQuery = RetrofitMake.getClient().create(RetrofitQuery.class);
            Call<MovieApiResponse> call = retrofitQuery.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<MovieApiResponse>(){
                @Override
                public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response){
                    List<MovieDetails> movies = response.body().getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                    recyclerView.smoothScrollToPosition(0);
                    if(swipeRefreshLayout.isRefreshing())
                    {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }

                @Override
                public void onFailure(Call<MovieApiResponse> call, Throwable t){
                    Log.d("Error",t.getMessage());
                    Toast.makeText(MainActivity.this,"Error Fetching top rated movies!",Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(Exception e){
            Log.d("Error",e.getMessage());
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.sort_order:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        checkSortOrder();
    }

    private void checkSortOrder(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sortOrder = preferences.getString(
                this.getString(R.string.pref_sort_order_key),
                this.getString(R.string.pref_most_popular)
        );
        if(sortOrder.equals(this.getString(R.string.pref_most_popular))){
            MainActivity.this.setTitle("Most Popular Movies");
            loadJSON();
        }
        else if(sortOrder.equals(this.getString(R.string.favorite))){
            MainActivity.this.setTitle("Favorite Movies");
            initViews2();
        }
        else
        {
            MainActivity.this.setTitle("Top Rated Movies");
            loadJSON1();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(movieList.isEmpty())
            checkSortOrder();
        else
            checkSortOrder();
    }

    private void getAllFavorite(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params){
                movieList.clear();
                movieList.addAll(getAllFavorite1());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                super.onPostExecute(aVoid);
                movieAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private List<MovieDetails> getAllFavorite1(){

        String sortOrder = FavoriteContract.FavoriteEntry._ID + " ASC";
        List<MovieDetails> favoriteList = new ArrayList<>();
        Cursor cursor = getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI, null, null, null, sortOrder);

        if (cursor.moveToFirst()){
            do {
                MovieDetails movie = new MovieDetails();
                movie.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID))));
                movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_USERRATING))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE)));

                favoriteList.add(movie);

            }while(cursor.moveToNext());
        }
        if(favoriteList == null)
            Toast.makeText(MainActivity.this, "There are no favorites yet!", Toast.LENGTH_SHORT).show();
        cursor.close();
        return favoriteList;
    }
}
