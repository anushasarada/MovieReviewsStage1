package com.example.sarada.moviereviews.presentation.views.activities

import NetworkStatusHelper
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.R
import com.example.sarada.moviereviews.presentation.adapters.MovieAdapter
import com.example.sarada.moviereviews.database.FavoriteContract
import com.example.sarada.moviereviews.databinding.ActivityMainBinding
import com.example.sarada.moviereviews.models.datac.MovieDetails
import com.example.sarada.moviereviews.models.sealedc.NetworkStatus
import com.example.sarada.moviereviews.presentation.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference

private var LOAD_FAVORITES: Boolean = true

class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private var movieList: MutableList<MovieDetails>? = null
    private var movieAdapter: MovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel

        initViews(false)
    }

    private fun initViews(showFavorites: Boolean) {

        binding.apply {
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark)
            swipeRefreshLayout.setOnRefreshListener {
                initViews(false)
                Snackbar.make(
                    binding.root, "Movies Refreshed",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.includedLayout.moviesRecyclerView.layoutManager = when(activity!!.resources.configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT -> GridLayoutManager(this, 2)
            else -> GridLayoutManager(this, 4)
        }

        movieList = ArrayList()
        movieAdapter = MovieAdapter(this)

        binding.includedLayout.moviesRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            adapter = movieAdapter
        }

        if(showFavorites)
            allFavorites
        else
            checkSortOrder()
    }

    private fun checkSortOrder() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val sortOrder = preferences.getString(
            this.getString(R.string.pref_sort_order_key),
            this.getString(R.string.pref_most_popular)
        )
        when (sortOrder) {
            this.getString(R.string.pref_most_popular) -> {
                this@MainActivity.title = resources.getString(R.string.most_popular_movies)
                loadJSON(resources.getString(R.string.most_popular_movies))
            }
            this.getString(R.string.favorite) -> {
                this@MainActivity.title = resources.getString(R.string.favorite)
                loadJSON(resources.getString(R.string.favorite))
            }
            else -> {
                this@MainActivity.title = resources.getString(R.string.top_rated_movies)
                loadJSON(resources.getString(R.string.top_rated_movies))
            }
        }
    }

    private val activity: Activity?
        get() {
            var context: Context? = this
            while (context is ContextWrapper) {
                if (context is Activity) return context
                context = context.baseContext
            }
            return null
        }

    private fun loadJSON(key: String) {
        try {
            if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "Please obtain API key from themoviedb.org",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            NetworkStatusHelper(this@MainActivity).observe(this) {
                when (it) {
                    NetworkStatus.Available -> {
                        viewModel.setTypeForMovies(key)
                        viewModel.movies.observe(this) { newMovies ->
                            binding.apply {
                                includedLayout.apply{
                                    moviesRecyclerView.apply {
                                        movieAdapter?.submitList(newMovies.results)
                                        smoothScrollToPosition(0)
                                        visibility = View.VISIBLE
                                    }
                                    check.visibility = View.GONE
                                }
                                if (swipeRefreshLayout.isRefreshing)
                                    swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    }
                    NetworkStatus.Unavailable -> {
                        binding.includedLayout.apply {
                            moviesRecyclerView.visibility = View.GONE
                            check.visibility = View.VISIBLE
                            check.text = "Please check your internet connection"
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("Error", e.message!!)
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_order -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, s: String) {
        checkSortOrder()
    }

    public override fun onResume() {
        super.onResume()
        if (movieList!!.isEmpty()) checkSortOrder() else checkSortOrder()
    }

    private val allFavorites: Unit
        get() {
            val task = MyAsyncTask(this)
            task.execute()
        }

    companion object {
        class MyAsyncTask internal constructor(context: MainActivity) :
            AsyncTask<Int, String, String?>() {

            private var resp: String? = null
            private val activityReference: WeakReference<MainActivity> = WeakReference(context)

            override fun doInBackground(vararg params: Int?): String? {
                activityReference.get()?.movieList!!.clear()
                activityReference.get()?.movieList!!.addAll(activityReference.get()?.allFavorite1!!)
                return null
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onPostExecute(result: String?) {

                val activity = activityReference.get()
                if (activity == null || activity.isFinishing) return
                activity.movieAdapter!!.notifyDataSetChanged()
            }

        }
    }

    @get:SuppressLint("Range")
    private val allFavorite1: List<MovieDetails>
        get() {
            val sortOrder = FavoriteContract.FavoriteEntry._ID + " ASC"
            val favoriteList: MutableList<MovieDetails> = ArrayList()
            val cursor = contentResolver.query(
                FavoriteContract.FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                sortOrder
            )
            if (cursor!!.moveToFirst()) {
                do {
                    val movie = MovieDetails()
                    movie.id =
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_MOVIEID))
                            .toInt()
                    movie.originalTitle =
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_TITLE))
                    movie.voteAverage =
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_USERRATING))
                            .toDouble()
                    movie.posterPath =
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH))
                    movie.overview =
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS))
                    movie.releaseDate =
                        cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE))
                    favoriteList.add(movie)
                } while (cursor.moveToNext())
            }
            @Suppress("SENSELESS_COMPARISON")
            if (favoriteList == null) Toast.makeText(
                this@MainActivity,
                "There are no favorites yet!",
                Toast.LENGTH_SHORT
            ).show()
            cursor.close()
            return favoriteList
        }
}