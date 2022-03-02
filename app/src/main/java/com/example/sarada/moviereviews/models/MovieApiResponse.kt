package com.example.sarada.moviereviews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import java.util.ArrayList

/**
 * Created by sarada on 3/11/2018.
 */
class MovieApiResponse : Parcelable {
    @SerializedName("page")
    var page = 0

    @SerializedName("results")
    var results: List<MovieDetails> = listOf()

    @SerializedName("total_results")
    var totalResults = 0

    @SerializedName("total_pages")
    var totalPages = 0
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(page)
        dest.writeTypedList(results)
        dest.writeInt(totalResults)
        dest.writeInt(totalPages)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        page = `in`.readInt()
        @Suppress("UNCHECKED_CAST")
        results = `in`.createTypedArrayList(MovieDetails.CREATOR)?.toList() as List<MovieDetails>
        totalResults = `in`.readInt()
        totalPages = `in`.readInt()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MovieApiResponse?> =
            object : Parcelable.Creator<MovieApiResponse?> {
                override fun createFromParcel(source: Parcel): MovieApiResponse? {
                    return MovieApiResponse(source)
                }

                override fun newArray(size: Int): Array<MovieApiResponse?> {
                    return arrayOfNulls(size)
                }
            }
    }

    /*companion object CREATOR : Parcelable.Creator<MovieApiResponse> {
        override fun createFromParcel(parcel: Parcel): MovieApiResponse {
            return MovieApiResponse(parcel)
        }

        override fun newArray(size: Int): Array<MovieApiResponse?> {
            return arrayOfNulls(size)
        }
    }*/
}