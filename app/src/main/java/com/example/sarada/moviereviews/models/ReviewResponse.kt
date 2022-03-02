package com.example.sarada.moviereviews.models

import com.google.gson.annotations.SerializedName
import com.example.sarada.moviereviews.models.Review

class ReviewResponse {
    @SerializedName("id")
    var id_review = 0

    @SerializedName("results")
    var results: List<Review>? = null
}