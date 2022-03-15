package com.example.sarada.moviereviews.models.datac

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewResponse(
    @Json(name = "id")
    var id_review: Int = 0,

    @Json(name = "results")
    var results: List<Review>? = null
):Parcelable