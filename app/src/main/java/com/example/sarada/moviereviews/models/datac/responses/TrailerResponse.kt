package com.example.sarada.moviereviews.models.datac.responses

import android.os.Parcelable
import com.example.sarada.moviereviews.models.datac.Trailer
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrailerResponse(
    @Json(name = "id")
    var idTrailer: Int = 0,

    @Json(name = "results")
    val results: List<Trailer>? = null

): Parcelable