package com.example.sarada.moviereviews.models

import com.google.gson.annotations.SerializedName

/**
 * Created by delaroy on 5/24/17.
 */
class Trailer(
    @field:SerializedName("key") var key: String, @field:SerializedName(
        "name"
    ) var name: String
)