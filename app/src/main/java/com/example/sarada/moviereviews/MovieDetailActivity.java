package com.example.sarada.moviereviews;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sarada.moviereviews.data.FavoriteContract;
import com.example.sarada.moviereviews.data.FavoriteDbHelper;
import com.example.sarada.moviereviews.models.MovieDetails;
import com.example.sarada.moviereviews.models.Trailer;
import com.example.sarada.moviereviews.models.TrailerResponse;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    TextView nameOfMovie;
    TextView plotSynopsis;
    TextView userRating;
    TextView releaseDate;
    ImageView imageView;
    RecyclerView recyclerView;
    private int movie_id;
    private String thumbnail, movieName, synopsis, rate, dateFromDB;
    MovieDetails movieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        nameOfMovie = findViewById(R.id.movie_title);
        plotSynopsis = findViewById(R.id.plot_synopsis);
        userRating = findViewById(R.id.userrating);
        releaseDate = findViewById(R.id.releasedate);
        imageView = findViewById(R.id.thumbnail_image_header);
        recyclerView = findViewById(R.id.recycler_view1);

        MovieDetailActivity.this.setTitle("Movie Details:");
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FavoriteDbHelper dbHelper = new FavoriteDbHelper(this);

        initCollapsingToolbar();

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra("movies"))
        {
            movieDetails = getIntent().getParcelableExtra("movies");
            thumbnail = movieDetails.getPosterPath();
            movieName = movieDetails.getOriginalTitle();
            synopsis = movieDetails.getOverview();
            rate = Double.toString(movieDetails.getVoteAverage());
            String rating = rate + "/10";
            movie_id = movieDetails.getId();

            dateFromDB = movieDetails.getReleaseDate();
            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date yourDate = null;
            try {
                yourDate = parser.parse(dateFromDB);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(yourDate);
            String dateOfRelease = Integer.toString(calendar.get(Calendar.YEAR));

            String poster = "https://image.tmdb.org/t/p/w500"+ thumbnail;

            Glide.with(this)
                    .load(poster)
                    .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
        }
        else
        {
            Toast.makeText(this,"No API data",Toast.LENGTH_SHORT).show();
        }

        MaterialFavoriteButton materialFavoriteButton = findViewById(R.id.favorite_button);

        if (Exists(movieName)){
            materialFavoriteButton.setFavorite(true);
            materialFavoriteButton.setOnFavoriteChangeListener(
                    (buttonView, favorite) -> {
                        if (favorite) {
                            saveFavorite();
                            Snackbar.make(buttonView, "Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            String stringId = Integer.toString(movie_id);
                            Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;
                            uri = uri.buildUpon().appendPath(stringId).build();
                            getContentResolver().delete(uri, null, null);

                            Snackbar.make(buttonView, "Removed from Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            materialFavoriteButton.setOnFavoriteChangeListener(
                    (buttonView, favorite) -> {
                        if (favorite) {
                            saveFavorite();
                            Snackbar.make(buttonView, "Added to Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        } else {
                            int movie_id = getIntent().getExtras().getInt("id");
                            String stringId = Integer.toString(movie_id);
                            Uri uri = FavoriteContract.FavoriteEntry.CONTENT_URI;
                            uri = uri.buildUpon().appendPath(stringId).build();
                            getContentResolver().delete(uri, null, null);
                            Snackbar.make(buttonView, "Removed from Favorite",
                                    Snackbar.LENGTH_SHORT).show();
                        }
                    });


        }
        initViews();
    }

    public boolean Exists(String searchItem) {



        String[] projection = {
                FavoriteContract.FavoriteEntry._ID,
                FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,
                FavoriteContract.FavoriteEntry.COLUMN_TITLE,
                FavoriteContract.FavoriteEntry.COLUMN_USERRATING,
                FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH,
                FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS

        };
        String selection = FavoriteContract.FavoriteEntry.COLUMN_TITLE + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";

        Cursor cursor = getContentResolver().query(FavoriteContract.FavoriteEntry.CONTENT_URI, projection, selection, selectionArgs, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void initViews(){
        List<Trailer> trailerList = new ArrayList<>();
        TrailerAdapter adapter = new TrailerAdapter(this, trailerList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadJSON();
    }

    private void loadJSON(){

        try{
            if (com.example.sarada.moviereviews.BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please obtain your API Key from themoviedb.org", Toast.LENGTH_SHORT).show();
                return;
            }
            RetrofitQuery apiService = RetrofitMake.getClient().create(RetrofitQuery.class);
            Call<TrailerResponse> call = apiService.getMovieTrailer(movie_id, com.example.sarada.moviereviews.BuildConfig.THE_MOVIE_DB_API_TOKEN);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                    List<Trailer> trailer = response.body().getResults();
                    if(trailer == null)
                        Toast.makeText(MovieDetailActivity.this,"There are no trailers for this movie.",Toast.LENGTH_SHORT).show();
                    recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailer));
                    recyclerView.smoothScrollToPosition(0);
                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MovieDetailActivity.this, "Error fetching trailer data", Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Log.d("Error", e.getMessage());
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void saveFavorite(){

        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, movie_id);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movieName);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_USERRATING, Double.valueOf(rate));
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, thumbnail);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, synopsis);
        values.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, dateFromDB);

        Uri uri = getContentResolver().insert(FavoriteContract.FavoriteEntry.CONTENT_URI, values);
        if(uri != null)
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.review_item) {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra("movieName", movieName);
            intent.putExtra("movieId", movie_id);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
