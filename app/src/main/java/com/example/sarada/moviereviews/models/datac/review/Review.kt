package com.example.sarada.moviereviews.models.datac.review

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

/**
 * Created by sarada on 4/4/2018.
 */
@Parcelize
data class Review(
    var author: String,
    var content: String,
    @Json(name = "author_details") var authorDetails: AuthorDetails
):Parcelable