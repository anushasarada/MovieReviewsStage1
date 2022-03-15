package com.example.sarada.moviereviews.models.datac

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Created by sarada on 4/4/2018.
 */
@Parcelize
data class Review(
    var author: String,
    var content: String
):Parcelable