package com.example.sarada.moviereviews.models.datac.responses

import android.os.Parcelable
import com.example.sarada.moviereviews.models.datac.MovieDetails
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by sarada on 3/11/2018.
 */
@Parcelize
data class MoviesResponse(
    @Json(name = "page") var page: Int = 0,

    @Json(name = "results")
    var results: List<MovieDetails> = listOf(),

    @Json(name = "total_results")
    var totalResults: Int = 0,

    @Json(name = "total_pages")
    var totalPages: Int = 0,

    ):Parcelable