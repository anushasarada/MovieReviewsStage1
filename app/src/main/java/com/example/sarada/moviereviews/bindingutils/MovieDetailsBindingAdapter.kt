package com.example.sarada.moviereviews.bindingutils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.sarada.moviereviews.models.datac.MovieDetails
import com.example.sarada.moviereviews.models.datac.Review
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("movieRatingText")
fun TextView.setMovieRatingText(item: MovieDetails?) {
    item?.let {
        val rate = java.lang.Double.toString(item.voteAverage!!)
        val rating = "$rate/10"
        text = rating
    }
}

@BindingAdapter("movieReleaseDateText")
fun TextView.setMovieReleaseDateText(item: MovieDetails?) {
    item?.let {
        val dateFromDB = item.releaseDate
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        var yourDate: Date? = null
        try {
            yourDate = parser.parse(dateFromDB!!)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = yourDate!!
        val dateOfRelease = calendar[Calendar.YEAR].toString()
        text = dateOfRelease
    }
}