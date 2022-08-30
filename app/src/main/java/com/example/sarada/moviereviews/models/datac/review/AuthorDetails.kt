package com.example.sarada.moviereviews.models.datac.review

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorDetails(
    var name: String,
    var username: String,
    @Json(name = "avatar_path") var avatarPath: String? = null,
    var rating: Int? = null
): Parcelable