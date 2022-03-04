package com.example.sarada.moviereviews

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sarada.moviereviews.models.MovieDetails
import android.os.Bundle
import com.example.sarada.moviereviews.R
import com.example.sarada.moviereviews.data.FavoriteDbHelper
import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.widget.Toast
import com.github.ivbaranov.mfb.MaterialFavoriteButton
import com.github.ivbaranov.mfb.MaterialFavoriteButton.OnFavoriteChangeListener
import com.google.android.material.snackbar.Snackbar
import com.example.sarada.moviereviews.data.FavoriteContract
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.example.sarada.moviereviews.models.Trailer
import com.example.sarada.moviereviews.TrailerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sarada.moviereviews.RetrofitQuery
import com.example.sarada.moviereviews.RetrofitMake
import com.example.sarada.moviereviews.models.TrailerResponse
import android.content.ContentValues
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.example.sarada.moviereviews.RetrofitMake.client
import com.example.sarada.moviereviews.ReviewActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MovieDetailActivity : AppCompatActivity() {
    var nameOfMovie: TextView? = null
    var plotSynopsis: TextView? = null
    var userRating: TextView? = null
    var releaseDate: TextView? = null
    var imageView: ImageView? = null
    var recyclerView: RecyclerView? = null
    private var movie_id = 0
    private var thumbnail: String? = null
    private var movieName: String? = null
    private var synopsis: String? = null
    private var rate: String? = null
    private var dateFromDB: String? = null
    var movieDetails: MovieDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        nameOfMovie = findViewById(R.id.movie_title)
        plotSynopsis = findViewById(R.id.plot_synopsis)
        userRating = findViewById(R.id.userrating)
        releaseDate = findViewById(R.id.releasedate)
        imageView = findViewById(R.id.thumbnail_image_header)
        recyclerView = findViewById(R.id.recycler_view1)
        this@MovieDetailActivity.title = "Movie Details:"
        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initCollapsingToolbar()
        val intentThatStartedThisActivity = intent
        if (intentThatStartedThisActivity.hasExtra("movies")) {
            movieDetails = intent.getParcelableExtra("movies")
            thumbnail = movieDetails!!.posterPath
            movieName = movieDetails!!.originalTitle
            synopsis = movieDetails!!.overview
            rate = java.lang.Double.toString(movieDetails!!.voteAverage!!)
            val rating = "$rate/10"
            movie_id = movieDetails!!.id!!
            dateFromDB = movieDetails!!.releaseDate
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            var yourDate: Date? = null
            try {
                yourDate = parser.parse(dateFromDB)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val calendar = Calendar.getInstance()
            calendar.time = yourDate
            val dateOfRelease = Integer.toString(calendar[Calendar.YEAR])
            val poster = "https://image.tmdb.org/t/p/w500$thumbnail"
            Glide.with(this)
                .load(poster)
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .into(imageView)
            nameOfMovie?.setText(movieName)
            plotSynopsis?.setText(synopsis)
            userRating?.setText(rating)
            releaseDate?.setText(dateOfRelease)
        } else {
            Toast.makeText(this, "No API data", Toast.LENGTH_SHORT).show()
        }
        val materialFavoriteButton = findViewById<MaterialFavoriteButton>(R.id.favorite_button)
        if (Exists(movieName)) {
            materialFavoriteButton.isFavorite = true
            materialFavoriteButton.setOnFavoriteChangeListener { buttonView: MaterialFavoriteButton?, favorite: Boolean ->
                if (favorite) {
                    saveFavorite()
                    Snackbar.make(
                        buttonView!!, "Added to Favorite",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val stringId = Integer.toString(movie_id)
                    var uri = FavoriteContract.FavoriteEntry.CONTENT_URI
                    uri = uri.buildUpon().appendPath(stringId).build()
                    contentResolver.delete(uri, null, null)
                    Snackbar.make(
                        buttonView!!, "Removed from Favorite",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            materialFavoriteButton.setOnFavoriteChangeListener { buttonView: MaterialFavoriteButton?, favorite: Boolean ->
                if (favorite) {
                    saveFavorite()
                    Snackbar.make(
                        buttonView!!, "Added to Favorite",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val movie_id = intent.extras!!.getInt("id")
                    val stringId = Integer.toString(movie_id)
                    var uri = FavoriteContract.FavoriteEntry.CONTENT_URI
                    uri = uri.buildUpon().appendPath(stringId).build()
                    contentResolver.delete(uri, null, null)
                    Snackbar.make(
                        buttonView!!, "Removed from Favorite",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        initViews()
    }

    fun Exists(searchItem: String?): Boolean {
        val projection = arrayOf(
            FavoriteContract.FavoriteEntry._ID,
            FavoriteContract.FavoriteEntry.COLUMN_MOVIEID,
            FavoriteContract.FavoriteEntry.COLUMN_TITLE,
            FavoriteContract.FavoriteEntry.COLUMN_USERRATING,
            FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH,
            FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS
        )
        val selection = FavoriteContract.FavoriteEntry.COLUMN_TITLE + " =?"
        val selectionArgs = arrayOf(searchItem)
        val limit = "1"
        val cursor = contentResolver.query(
            FavoriteContract.FavoriteEntry.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            limit
        )
        val exists = cursor!!.count > 0
        cursor.close()
        return exists
    }

    private fun initCollapsingToolbar() {
        val collapsingToolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
        collapsingToolbarLayout.title = " "
        val appBarLayout = findViewById<AppBarLayout>(R.id.appbar)
        appBarLayout.setExpanded(true)
        appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.title = getString(R.string.movie_name)
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun initViews() {
        val trailerList: List<Trailer> = ArrayList()
        val adapter = TrailerAdapter(this, trailerList)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.adapter = adapter
        adapter.notifyDataSetChanged()
        loadJSON()
    }

    private fun loadJSON() {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please obtain your API Key from themoviedb.org",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            val apiService = client?.create(
                RetrofitQuery::class.java
            )
            val call = apiService?.getMovieTrailer(movie_id, BuildConfig.THE_MOVIE_DB_API_TOKEN)
            call?.enqueue(object : Callback<TrailerResponse?> {
                override fun onResponse(
                    call: Call<TrailerResponse?>?,
                    response: Response<TrailerResponse?>?
                ) {
                    val trailer = response?.body()?.results
                    if (trailer == null) Toast.makeText(
                        this@MovieDetailActivity,
                        "There are no trailers for this movie.",
                        Toast.LENGTH_SHORT
                    ).show()
                    recyclerView!!.adapter = trailer?.let { TrailerAdapter(applicationContext, it) }
                    recyclerView!!.smoothScrollToPosition(0)
                }

                override fun onFailure(call: Call<TrailerResponse?>, t: Throwable) {
                    Log.d("Error", t.message!!)
                    Toast.makeText(
                        this@MovieDetailActivity,
                        "Error fetching trailer data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } catch (e: Exception) {
            Log.d("Error", e.message!!)
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun saveFavorite() {
        val values = ContentValues()
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, movie_id)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movieName)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_USERRATING, java.lang.Double.valueOf(rate))
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, thumbnail)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, synopsis)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, dateFromDB)
        val uri = contentResolver.insert(FavoriteContract.FavoriteEntry.CONTENT_URI, values)
        if (uri != null) Toast.makeText(baseContext, uri.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.review_item) {
            val intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra("movieName", movieName)
            intent.putExtra("movieId", movie_id)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}