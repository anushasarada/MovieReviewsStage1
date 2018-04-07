package com.example.sarada.moviereviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sarada.moviereviews.models.Review;
import com.example.sarada.moviereviews.models.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {

    RecyclerView recyclerView2;
    int movie_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        recyclerView2 = findViewById(R.id.recycler_view2);
        movie_id = getIntent().getExtras().getInt("movieId");
        ReviewActivity.this.setTitle("Reviews of \n"+getIntent().getExtras().getString("movieName"));
        Toast.makeText(ReviewActivity.this,getIntent().getExtras().getString("movieName"),Toast.LENGTH_SHORT).show();
        initViews1();
    }

    private void initViews1(){
        List<Review> reviewList = new ArrayList<>();
        ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviewList);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(layoutManager1);
        recyclerView2.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
        loadJSON1();
    }

    private void loadJSON1(){

        try{
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain your API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            RetrofitMake Client = new RetrofitMake();
            RetrofitQuery apiService = RetrofitMake.getClient().create(RetrofitQuery.class);
            Call<ReviewResponse> call = apiService.getMovieReviews(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                    List<Review> review = response.body().getResults();
                    recyclerView2.setAdapter(new ReviewAdapter(getApplicationContext(), review));
                    recyclerView2.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(ReviewActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.review_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.home_item:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
