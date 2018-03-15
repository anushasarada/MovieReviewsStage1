package com.example.sarada.moviereviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.AppBarLayout.OnOffsetChangedListener;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity {

    TextView nameOfMovie, plotSynopsis, userRating, releaseDate;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        imageView = findViewById(R.id.thumbnail_image_header);
        nameOfMovie = findViewById(R.id.movie_title);
        plotSynopsis = findViewById(R.id.plot_synopsis);
        userRating = findViewById(R.id.userrating);
        releaseDate = findViewById(R.id.releasedate);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity.hasExtra("original_title"))
        {
            String thumbnail = getIntent().getExtras().getString("poster_path");
            String movieName = getIntent().getExtras().getString("original_title");
            String synopsis = getIntent().getExtras().getString("overview");
            String rating = (getIntent().getExtras().getString("vote_average"))+"/10";

            String dateFromDB = getIntent().getExtras().getString("release_date");
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


            Glide.with(this)
                    .load(thumbnail)
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
    }

    private void initCollapsingToolbar()
    {
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
}
