package com.example.sarada.moviereviews.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.MoviesApi
import com.example.sarada.moviereviews.models.datac.MovieApiResponse
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {

    private var TOP_RATED_MOVIES: String = "Top Rated Movies"
    private var MOST_POPULAR_MOVIES: String = "Most Popular Movies"

    private val _movies = MutableLiveData<MovieApiResponse>()
    val movies: LiveData<MovieApiResponse>
        get() = _movies

    lateinit var key: String

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        key = TOP_RATED_MOVIES
        getMovies()
        _movies.value = MovieApiResponse()
    }

    private fun getMovies() {

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
        viewModelJob.cancel()
    }
}