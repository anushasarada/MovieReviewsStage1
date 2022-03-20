package com.example.sarada.moviereviews.models.datac.responses

import android.os.Parcelable
import com.example.sarada.moviereviews.models.datac.Review
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReviewResponse(
    @Json(name = "id")
    var id_review: Int = 0,

    @Json(name = "results")
    var results: List<Review>? = null
):Parcelable