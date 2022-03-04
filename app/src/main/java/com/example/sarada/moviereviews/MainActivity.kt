package com.example.sarada.moviereviews

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
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.sarada.moviereviews.RetrofitMake.client
import com.example.sarada.moviereviews.data.FavoriteContract
import com.example.sarada.moviereviews.databinding.ActivityMainBinding
import com.example.sarada.moviereviews.models.MovieApiResponse
import com.example.sarada.moviereviews.models.MovieDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private lateinit var binding: ActivityMainBinding

    private var movieList: MutableList<MovieDetails>? = null
    private var movieAdapter: MovieAdapter? = null

    private var TOP_RATED_MOVIES: String = "Top Rated Movies"
    private var MOST_POPULAR_MOVIES: String = "Most Popular Movies"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViews()

        binding.swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark)
        binding.swipeRefreshLayout.setOnRefreshListener {
            initViews()
            Toast.makeText(this@MainActivity, "Movies Refreshed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initViews() {

        if (activity!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 2)
            binding.includedLayout.recyclerView.layoutManager = mLayoutManager
        } else {
            val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 4)
            binding.includedLayout.recyclerView.layoutManager = mLayoutManager
        }

        movieList = ArrayList()
        movieAdapter = MovieAdapter(this, movieList as ArrayList<MovieDetails>)

        binding.apply {
            includedLayout.recyclerView.itemAnimator = DefaultItemAnimator()
            includedLayout.recyclerView.adapter = movieAdapter
        }
        movieAdapter!!.notifyDataSetChanged()

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
                initViews2()
            }
            else -> {
                this@MainActivity.title = TOP_RATED_MOVIES
                loadJSON(TOP_RATED_MOVIES)
            }
        }
    }

    private fun initViews2() {

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

        allFavorite
    }

    val activity: Activity?
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
            val retrofitQuery = client?.create(
                RetrofitQuery::class.java
            )
            val call = when (key) {
                TOP_RATED_MOVIES -> retrofitQuery?.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                MOST_POPULAR_MOVIES -> retrofitQuery?.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                else -> null
            }
            call?.enqueue(object : Callback<MovieApiResponse?> {
                override fun onResponse(
                    call: Call<MovieApiResponse?>,
                    response: Response<MovieApiResponse?>
                ) {
                    if (response.body() != null) {
                        val movies: List<MovieDetails> = response.body()!!.results
                        binding.includedLayout.recyclerView.adapter = MovieAdapter(applicationContext, movies)
                        binding.includedLayout.recyclerView.smoothScrollToPosition(0)
                        if (binding.swipeRefreshLayout.isRefreshing)
                            binding.swipeRefreshLayout.isRefreshing = false
                    }
                }

                override fun onFailure(call: Call<MovieApiResponse?>, t: Throwable) {
                    Log.d("Error", t.message!!)
                    binding.includedLayout.check.visibility = View.VISIBLE
                    Toast.makeText(
                        this@MainActivity,
                        """Error Fetching $key !""",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    private val allFavorite: Unit
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