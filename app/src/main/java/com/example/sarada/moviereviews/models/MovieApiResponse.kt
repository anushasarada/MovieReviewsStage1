package com.example.sarada.moviereviews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

/**
 * Created by sarada on 3/11/2018.
 */
@Parcelize
data class MovieApiResponse(
    @Json(name = "page") var page: Int = 0,

    @Json(name = "results")
    var results: List<MovieDetails> = listOf(),

    @Json(name = "total_results")
    var totalResults: Int = 0,

    @Json(name = "total_pages")
    var totalPages: Int = 0,

):Parcelable