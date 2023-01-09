package com.example.sarada.moviereviews.presentation.views.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.R
import com.example.sarada.moviereviews.presentation.adapters.TrailerAdapter
import com.example.sarada.moviereviews.database.FavoriteContract
import com.example.sarada.moviereviews.database.FavoritesDatabase
import com.example.sarada.moviereviews.databinding.ActivityMovieDetailBinding
import com.example.sarada.moviereviews.factories.MovieDetailViewModelFactory
import com.example.sarada.moviereviews.models.datac.MovieDetails
import com.example.sarada.moviereviews.models.datac.Trailer
import com.example.sarada.moviereviews.presentation.viewmodels.MovieDetailViewModel
import com.github.ivbaranov.mfb.MaterialFavoriteButton
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailBinding
    private lateinit var viewModel: MovieDetailViewModel
    /*private val viewModel: MovieDetailViewModel by lazy {
        ViewModelProvider(this).get(MovieDetailViewModel::class.java)
    }*/

    private var movieId = 0
    private var thumbnail: String? = null
    private var movieName: String? = null
    private var synopsis: String? = null
    private var rate: String? = null
    private var dateFromDB: String? = null
    var movieDetails: MovieDetails? = null
    val trailerAdapter = TrailerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        binding.lifecycleOwner = this

        val application = requireNotNull(this@MovieDetailActivity).application
        val dataSource = FavoritesDatabase.getInstance(application).favoritesDatabaseDao
        val viewModelFactory = MovieDetailViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel::class.java)

        binding.movieDetailViewModel = viewModel

        this@MovieDetailActivity.title = "Movie Details:"

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initCollapsingToolbar()

        if (intent.hasExtra("movies")) {

            movieDetails = intent.getParcelableExtra("movies")

            thumbnail = movieDetails!!.posterPath
            movieName = movieDetails!!.originalTitle
            synopsis = movieDetails!!.overview
            rate = java.lang.Double.toString(movieDetails!!.voteAverage!!)
            movieId = movieDetails!!.id!!
            dateFromDB = movieDetails!!.releaseDate

            val poster = "https://image.tmdb.org/t/p/w500$thumbnail"
            Glide.with(this)
                .load(poster)
                .apply(RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                .into(binding.movieContentDetailActivity.thumbnailImageHeader)

            binding.movieContentDetailActivity.movieDetails = movieDetails
            binding.executePendingBindings()

        } else {
            Toast.makeText(this, "No API data", Toast.LENGTH_SHORT).show()
        }

        if (Exists(movieName)) {
            binding.movieContentDetailActivity.favoriteButton.isFavorite = true
            binding.movieContentDetailActivity.favoriteButton.setOnFavoriteChangeListener { buttonView: MaterialFavoriteButton?, favorite: Boolean ->
                if (favorite) {
                    saveFavorite()
                    Snackbar.make(
                        buttonView!!, "Added to Favorite",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    val stringId = movieId.toString()
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
            binding.movieContentDetailActivity.favoriteButton.setOnFavoriteChangeListener { buttonView: MaterialFavoriteButton?, favorite: Boolean ->
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

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        binding.movieContentDetailActivity.apply{
            trailersRecyclerView.layoutManager = mLayoutManager
            trailersRecyclerView.adapter = trailerAdapter
        }
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

            viewModel.movieId.value = movieId
            viewModel.trailer.observe(this) { newTrailers ->
                if (newTrailers.results != null && newTrailers.results.isNotEmpty()) {
                    trailerAdapter.submitList(newTrailers.results)
                    binding.movieContentDetailActivity.apply {
                        trailersRecyclerView.smoothScrollToPosition(0)
                        trailersHeader.visibility = View.VISIBLE
                        trailersRecyclerView.visibility = View.VISIBLE
                        noTrailersTextView.visibility = View.GONE
                    }
                }else{
                    binding.movieContentDetailActivity.apply {
                        trailersHeader.visibility = View.GONE
                        trailersRecyclerView.visibility = View.GONE
                        noTrailersTextView.visibility = View.VISIBLE
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("Error", e.message!!)
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveFavorite() {
        val values = ContentValues()
        values.put(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID, movieId)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_TITLE, movieName)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_USERRATING, java.lang.Double.valueOf(rate))
        values.put(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH, thumbnail)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS, synopsis)
        values.put(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE, dateFromDB)
        contentResolver.insert(FavoriteContract.FavoriteEntry.CONTENT_URI, values)
        //val uri = contentResolver.insert(FavoriteContract.FavoriteEntry.CONTENT_URI, values)
        //if (uri != null) Toast.makeText(baseContext, uri.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.review_item) {
            val intent = Intent(this, ReviewActivity::class.java)
            intent.putExtra("movieName", movieName)
            intent.putExtra("movieId", viewModel.movieId.value)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}