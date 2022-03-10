package com.example.sarada.moviereviews.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class Favorite(

    @PrimaryKey(autoGenerate = true)
    var _ID: Long = 0L,

    @ColumnInfo var TABLE_NAME: String = "favorite",
    @ColumnInfo var COLUMN_MOVIEID: String = "movieid",
    @ColumnInfo var COLUMN_TITLE: String = "title",
    @ColumnInfo var COLUMN_USERRATING: String = "userrating",
    @ColumnInfo var COLUMN_POSTER_PATH: String = "posterpath",
    @ColumnInfo var COLUMN_PLOT_SYNOPSIS: String = "overview",
    @ColumnInfo var COLUMN_RELEASE_DATE: String = "date"
)