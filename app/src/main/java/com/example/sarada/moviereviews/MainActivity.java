package com.example.sarada.moviereviews;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private RecyclerView recyclerView;
    private List<MovieDetails> movieList;
    private MovieAdapter movieAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
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
        recyclerView = findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this,movieList);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(mLayoutManager);
            Toast.makeText(this,"layout manager set",Toast.LENGTH_SHORT).show();
        }
        else
        {
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,4);
            recyclerView.setLayoutManager(mLayoutManager);
            Toast.makeText(this,"layout manager set",Toast.LENGTH_SHORT).show();
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);
        Toast.makeText(this,"adapter set",Toast.LENGTH_SHORT).show();
        movieAdapter.notifyDataSetChanged();
        checkSortOrder();
    }

    private void loadJSON(){

        Toast.makeText(getApplicationContext(),"App started.",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this,"Error Fetching data!",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this,"Error Fetching data!",Toast.LENGTH_SHORT).show();
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
        if(sortOrder.equals(this.getString(R.string.pref_most_popular)))
            loadJSON();
        else
            loadJSON1();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(movieList.isEmpty())
            checkSortOrder();
    }
}
