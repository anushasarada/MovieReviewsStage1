package com.example.sarada.moviereviews.database

import android.net.Uri
import android.provider.BaseColumns

object FavoriteContract {
    const val AUTHORITY = "com.example.sarada.moviereviews"
    val BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY)
    const val PATH_FAVORITE = "favorite"

    object FavoriteEntry : BaseColumns {
        @JvmField
        val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build()
        const val _ID = "_id"
        const val TABLE_NAME = "favorite"
        const val COLUMN_MOVIEID = "movieid"
        const val COLUMN_TITLE = "title"
        const val COLUMN_USERRATING = "userrating"
        const val COLUMN_POSTER_PATH = "posterpath"
        const val COLUMN_PLOT_SYNOPSIS = "overview"
        const val COLUMN_RELEASE_DATE = "date"
    }
}