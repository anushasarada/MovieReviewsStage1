package com.example.sarada.moviereviews.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.network.MoviesApi
import com.example.sarada.moviereviews.models.datac.responses.ReviewResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ReviewViewModel: ViewModel() {

    private val _reviews = MutableLiveData<ReviewResponse>()
    val reviews: LiveData<ReviewResponse>
        get() = _reviews

    var movieId: MutableLiveData<Int> = MutableLiveData<Int>()

    val viewModelJob = Job()
    val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getMovies()
        _reviews.value = ReviewResponse()
    }

    private fun getMovies() {

        coroutineScope.launch {
            val getMoviesDeferred =
                MoviesApi.RETROFIT_SERVICE.getMovieReviews(movieId.value!!, BuildConfig.THE_MOVIE_DB_API_TOKEN)
            try {
                _reviews.value = getMoviesDeferred.await()
            } catch (t: Throwable) {
                Log.d("Error fetching data", t.message!!)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}