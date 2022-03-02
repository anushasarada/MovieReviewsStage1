package com.example.sarada.moviereviews.models

import com.google.gson.annotations.SerializedName
import com.example.sarada.moviereviews.models.Trailer

class TrailerResponse {
    @SerializedName("id")
    var idTrailer = 0
        private set

    @SerializedName("results")
    val results: List<Trailer>? = null
    fun seIdTrailer(id_trailer: Int) {
        idTrailer = id_trailer
    }
}