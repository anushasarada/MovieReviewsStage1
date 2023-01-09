package com.example.sarada.moviereviews.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sarada.moviereviews.database.FavoritesDatabaseDao
import com.example.sarada.moviereviews.presentation.viewmodels.MovieDetailViewModel
import java.lang.IllegalArgumentException

class MovieDetailViewModelFactory(
    private val dataSource: FavoritesDatabaseDao,
    private val application: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MovieDetailViewModel::class.java)){
            return MovieDetailViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}