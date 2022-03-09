package com.example.sarada.moviereviews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.example.sarada.moviereviews.models.Review
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewResponse(
    @Json(name = "id")
    var id_review: Int = 0,

    @Json(name = "results")
    var results: List<Review>? = null
):Parcelable