package com.example.sarada.moviereviews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.example.sarada.moviereviews.models.MovieDetails
import java.util.ArrayList

/**
 * Created by sarada on 3/8/2018.
 */
class MovieDetails : Parcelable {
    @SerializedName("poster_path")
    var posterPath: String? = null

    @SerializedName("adult")
    var adult: Boolean? = null

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("release_date")
    var releaseDate: String? = null

    @SerializedName("genre_ids")
    var genreIds: List<Int?> = ArrayList()

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("original_title")
    var originalTitle: String? = null

    @SerializedName("original_language")
    var originalLanguage: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("backdrop_path")
    var backdropPath: String? = null

    @SerializedName("popularity")
    var popularity: Double? = null

    @SerializedName("vote_count")
    var voteCount: Int? = null

    @SerializedName("video")
    var video: Boolean? = null

    @SerializedName("vote_average")
    var voteAverage: Double? = null

    constructor() {}
    constructor(
        posterPath: String?,
        adult: Boolean,
        overview: String?,
        releaseDate: String?,
        genreIds: List<Int?>,
        id: Int?,
        originalTitle: String?,
        originalLanguage: String?,
        title: String?,
        backdropPath: String?,
        popularity: Double?,
        voteCount: Int?,
        video: Boolean?,
        voteAverage: Double?
    ) {
        this.posterPath = posterPath
        this.adult = adult
        this.overview = overview
        this.releaseDate = releaseDate
        this.genreIds = genreIds
        this.id = id
        this.originalTitle = originalTitle
        this.originalLanguage = originalLanguage
        this.title = title
        this.backdropPath = backdropPath
        this.popularity = popularity
        this.voteCount = voteCount
        this.video = video
        this.voteAverage = voteAverage
    }

    var baseImageURL: String? = "https://image.tmdb.org/t/p/w500"
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(posterPath)
        dest.writeValue(adult)
        dest.writeString(overview)
        dest.writeString(releaseDate)
        dest.writeList(genreIds)
        dest.writeValue(id)
        dest.writeString(originalTitle)
        dest.writeString(originalLanguage)
        dest.writeString(title)
        dest.writeString(backdropPath)
        dest.writeValue(popularity)
        dest.writeValue(voteCount)
        dest.writeValue(video)
        dest.writeValue(voteAverage)
        dest.writeString(baseImageURL)
    }

    protected constructor(`in`: Parcel) {
        posterPath = `in`.readString()
        adult = `in`.readValue(Boolean::class.java.classLoader) as Boolean?
        overview = `in`.readString()
        releaseDate = `in`.readString()
        genreIds = ArrayList()
        `in`.readList(genreIds, Int::class.java.classLoader)
        id = `in`.readValue(Int::class.java.classLoader) as Int?
        originalTitle = `in`.readString()
        originalLanguage = `in`.readString()
        title = `in`.readString()
        backdropPath = `in`.readString()
        popularity = `in`.readValue(Double::class.java.classLoader) as Double?
        voteCount = `in`.readValue(Int::class.java.classLoader) as Int?
        video = `in`.readValue(Boolean::class.java.classLoader) as Boolean?
        voteAverage = `in`.readValue(Double::class.java.classLoader) as Double?
        baseImageURL = `in`.readString()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MovieDetails?> = object : Parcelable.Creator<MovieDetails?> {
            override fun createFromParcel(source: Parcel): MovieDetails {
                return MovieDetails(source)
            }

            override fun newArray(size: Int): Array<MovieDetails?> {
                return arrayOfNulls(size)
            }
        }
    }
}