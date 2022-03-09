package com.example.sarada.moviereviews.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.MoviesApi
import com.example.sarada.moviereviews.models.ReviewResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ReviewActivityViewModel: ViewModel() {

    private val _reviews = MutableLiveData<ReviewResponse>()
    val reviews: LiveData<ReviewResponse>
        get() = _reviews

    lateinit var movieId: MutableLiveData<Int>

    init {
        getMovies()
        _reviews.value = ReviewResponse()
    }

    private fun getMovies() {
        val viewModelJob = Job()
        val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

        coroutineScope.launch {
            val getMoviesDeferred =
                MoviesApi.retrofitService.getMovieReviews(movieId.value!!, BuildConfig.THE_MOVIE_DB_API_TOKEN)
            try {
                _reviews.value = getMoviesDeferred.await()
            } catch (t: Throwable) {
                Log.d("Error fetching data", t.message!!)
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
    }

}