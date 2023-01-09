package com.example.sarada.moviereviews.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.network.MoviesApi
import com.example.sarada.moviereviews.models.datac.responses.MoviesResponse
import kotlinx.coroutines.*

class MainViewModel: ViewModel() {

    private var TOP_RATED_MOVIES: String = "Top Rated Movies"
    private var MOST_POPULAR_MOVIES: String = "Most Popular Movies"
    private var FAVORITE_MOVIES: String = "Favorite Movies"

    private val _movies = MutableLiveData<MoviesResponse>()
    val movies: LiveData<MoviesResponse>
        get() = _movies

    lateinit var key: String

    init {
        key = TOP_RATED_MOVIES
        getMovies()
        _movies.value = MoviesResponse()
    }

    private fun getMovies() {

        viewModelScope.launch {

            val getMoviesDeferred = when (key) {
                TOP_RATED_MOVIES -> MoviesApi.RETROFIT_SERVICE.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                MOST_POPULAR_MOVIES -> MoviesApi.RETROFIT_SERVICE.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN)
                //FAVORITE_MOVIES -> allFavorites
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

}