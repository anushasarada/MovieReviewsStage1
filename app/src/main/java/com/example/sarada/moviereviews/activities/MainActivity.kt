package com.example.sarada.moviereviews.activities

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.R
import com.example.sarada.moviereviews.adapters.MovieAdapter
import com.example.sarada.moviereviews.database.FavoriteContract
import com.example.sarada.moviereviews.databinding.ActivityMainBinding
import com.example.sarada.moviereviews.models.MovieDetails
import com.example.sarada.moviereviews.viewmodels.MainViewModel
import java.lang.ref.WeakReference

private var TOP_RATED_MOVIES: String = "Top Rated Movies"
private var MOST_POPULAR_MOVIES: String = "Most Popular Movies"
private var LOAD_FAVORITES: Boolean = true

class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var movieList: MutableList<MovieDetails>? = null
    private var movieAdapter: MovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.mainViewModel = viewModel
        initViews(false)

        binding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark)
        binding.swipeRefreshLayout.setOnRefreshListener {
            initViews(false)
            Toast.makeText(this@MainActivity, "Movies Refreshed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews(showFavorites: Boolean) {

        if (activity!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.includedLayout.recyclerView.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.includedLayout.recyclerView.layoutManager = GridLayoutManager(this, 4)
        }

        movieList = ArrayList()
        movieAdapter = MovieAdapter(this, movieList as ArrayList<MovieDetails>)

        binding.apply {
            includedLayout.recyclerView.itemAnimator = DefaultItemAnimator()
            includedLayout.recyclerView.adapter = movieAdapter
        }
        movieAdapter!!.notifyDataSetChanged()

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
                this@MainActivity.title = MOST_POPULAR_MOVIES
                loadJSON(MOST_POPULAR_MOVIES)
            }
            this.getString(R.string.favorite) -> {
                this@MainActivity.title = "Favorite Movies"
                initViews(LOAD_FAVORITES)
            }
            else -> {
                this@MainActivity.title = TOP_RATED_MOVIES
                loadJSON(TOP_RATED_MOVIES)
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

            viewModel.movies.observe(this, Observer { newMovies ->
                binding.includedLayout.recyclerView.adapter = MovieAdapter(applicationContext, newMovies.results)
                binding.includedLayout.recyclerView.smoothScrollToPosition(0)
                if (binding.swipeRefreshLayout.isRefreshing)
                    binding.swipeRefreshLayout.isRefreshing = false
                binding.includedLayout.check.visibility = View.INVISIBLE
            })

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