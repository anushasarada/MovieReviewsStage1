package com.example.sarada.moviereviews.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.MoviesApi
import com.example.sarada.moviereviews.models.datac.MovieApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var TOP_RATED_MOVIES: String = "Top Rated Movies"
    private var MOST_POPULAR_MOVIES: String = "Most Popular Movies"

    private val _movies = MutableLiveData<MovieApiResponse>()
    val movies: LiveData<MovieApiResponse>
        get() = _movies

    lateinit var key: String

    init {
        key = TOP_RATED_MOVIES
        getMovies()
        _movies.value = MovieApiResponse()
    }

    private fun getMovies() {
        val viewModelJob = Job()
        val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

        coroutineScope.launch {

            val getMoviesDeferred = when (key) {
                TOP_RATED_MOVIES -> MoviesApi.retrofitService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                MOST_POPULAR_MOVIES -> MoviesApi.retrofitService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                else -> null
            }

            try {
                if (getMoviesDeferred != null) {
                    _movies.value = getMoviesDeferred.await()
                }
            } catch (t: Throwable) {
                Log.d("Error fetching data", t.message!!)
            }
        }
    }

    fun setTypeForMovies(key: String) {
        this.key = key
    }

    override fun onCleared() {
        super.onCleared()
    }
}