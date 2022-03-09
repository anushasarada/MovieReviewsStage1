package com.example.sarada.moviereviews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.example.sarada.moviereviews.models.MovieDetails
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

/**
 * Created by sarada on 3/8/2018.
 */
@Parcelize
data class MovieDetails(
    @Json(name = "poster_path")
    var posterPath: String? = null,

    @Json(name = "adult")
    var adult: Boolean? = null,

    @Json(name = "overview")
    var overview: String? = null,

    @Json(name = "release_date")
    var releaseDate: String? = null,

    @Json(name = "genre_ids")
    var genreIds: List<Int?> = ArrayList(),

    @Json(name = "id")
    var id: Int? = null,

    @Json(name = "original_title")
    var originalTitle: String? = null,

    @Json(name = "original_language")
    var originalLanguage: String? = null,

    @Json(name = "title")
    var title: String? = null,

    @Json(name = "backdrop_path")
    var backdropPath: String? = null,

    @Json(name = "popularity")
    var popularity: Double? = null,

    @Json(name = "vote_count")
    var voteCount: Int? = null,

    @Json(name = "video")
    var video: Boolean? = null,

    @Json(name = "vote_average")
    var voteAverage: Double? = null,

):Parcelable