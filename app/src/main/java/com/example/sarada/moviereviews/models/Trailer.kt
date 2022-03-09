package com.example.sarada.moviereviews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by delaroy on 5/24/17.
 */
@Parcelize
data class Trailer(
    @Json(name = "key") var key: String,
    @Json(name = "name") var name: String
) : Parcelable