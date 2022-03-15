package com.example.sarada.moviereviews.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.sarada.moviereviews.BuildConfig
import com.example.sarada.moviereviews.MoviesApi
import com.example.sarada.moviereviews.database.FavoritesDatabaseDao
import com.example.sarada.moviereviews.models.datac.TrailerResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    val dataSource: FavoritesDatabaseDao,
    application: Application
): AndroidViewModel(application) {

    private val _trailer = MutableLiveData<TrailerResponse>()
    val trailer: LiveData<TrailerResponse>
        get() = _trailer

    var movieId: MutableLiveData<Int> = MutableLiveData<Int>()

    init {
        getTrailers()
        _trailer.value = TrailerResponse()
    }

    private fun getTrailers() {
        val viewModelJob = Job()
        val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

        coroutineScope.launch {
            val getUserDetailsDeferred = MoviesApi.retrofitService.getMovieTrailer(movieId.value!!, BuildConfig.THE_MOVIE_DB_API_TOKEN)
            try {
                _trailer.value = getUserDetailsDeferred.await()
            } catch (t: Throwable) {
                Log.d("Error fetching data", t.message!!)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

}