package com.example.sarada.moviereviews.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.MoviesApi
import com.example.sarada.moviereviews.models.MovieApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _movies = MutableLiveData<MovieApiResponse>()
    val movies: LiveData<MovieApiResponse>
        get() = _movies

    /*val call = when (key) {
        TOP_RATED_MOVIES -> retrofitQuery?.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN)
        MOST_POPULAR_MOVIES -> retrofitQuery?.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN)
        else -> null
    }*/

    init {
        getMovies()
        _movies.value = MovieApiResponse()
    }

    private fun getMovies() {
        val viewModelJob = Job()
        val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

        coroutineScope.launch {
            val getMoviesDeferred =
                MoviesApi.retrofitService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_TOKEN)
            try {
                _movies.value = getMoviesDeferred.await()
            } catch (t: Throwable) {
                Log.d("Error fetching data", t.message!!)
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
    }
}