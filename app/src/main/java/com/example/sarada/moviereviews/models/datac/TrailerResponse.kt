package com.example.sarada.moviereviews.models.datac

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrailerResponse(
    @Json(name = "id")
    var idTrailer: Int = 0,

    @Json(name = "results")
    val results: List<Trailer>? = null

): Parcelable