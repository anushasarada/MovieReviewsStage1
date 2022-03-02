package com.example.sarada.moviereviews.models

import com.google.gson.annotations.SerializedName

/**
 * Created by sarada on 4/4/2018.
 */
class Review(
    @field:SerializedName("author") var author: String, @field:SerializedName(
        "content"
    ) var content: String
)