package com.example.sarada.moviereviews.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import android.os.Parcel
import com.example.sarada.moviereviews.models.MovieDetails
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

/**
 * Created by sarada on 3/8/2018.
 */
@Parcelize
data class MovieDetails(
    @Json(name = "poster_path")
    var posterPath: String? = null,

    @Json(name = "adult")
    var adult: Boolean? = null,

    @Json(name = "overview")
    var overview: String? = null,

    @Json(name = "release_date")
    var releaseDate: String? = null,

    @Json(name = "genre_ids")
    var genreIds: List<Int?> = ArrayList(),

    @Json(name = "id")
    var id: Int? = null,

    @Json(name = "original_title")
    var originalTitle: String? = null,

    @Json(name = "original_language")
    var originalLanguage: String? = null,

    @Json(name = "title")
    var title: String? = null,

    @Json(name = "backdrop_path")
    var backdropPath: String? = null,

    @Json(name = "popularity")
    var popularity: Double? = null,

    @Json(name = "vote_count")
    var voteCount: Int? = null,

    @Json(name = "video")
    var video: Boolean? = null,

    @Json(name = "vote_average")
    var voteAverage: Double? = null,
/*
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
    }*/
/*
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
    }*/
):Parcelable