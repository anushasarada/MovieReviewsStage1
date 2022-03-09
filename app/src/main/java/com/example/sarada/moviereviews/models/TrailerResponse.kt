package com.example.sarada.moviereviews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.example.sarada.moviereviews.models.Trailer
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrailerResponse(
    @Json(name = "id")
    var idTrailer: Int = 0,

    @Json(name = "results")
    val results: List<Trailer>? = null

): Parcelable