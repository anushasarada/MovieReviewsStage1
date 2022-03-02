package com.example.sarada.moviereviews.data

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.example.sarada.moviereviews.data.FavoriteDbHelper
import android.database.sqlite.SQLiteDatabase
import com.example.sarada.moviereviews.data.FavoriteContract
import com.example.sarada.moviereviews.data.FavoriteContract.FavoriteEntry.COLUMN_MOVIEID
import com.example.sarada.moviereviews.data.FavoriteContract.FavoriteEntry.COLUMN_PLOT_SYNOPSIS
import com.example.sarada.moviereviews.data.FavoriteContract.FavoriteEntry.COLUMN_POSTER_PATH
import com.example.sarada.moviereviews.data.FavoriteContract.FavoriteEntry.COLUMN_RELEASE_DATE
import com.example.sarada.moviereviews.data.FavoriteContract.FavoriteEntry.COLUMN_TITLE
import com.example.sarada.moviereviews.data.FavoriteContract.FavoriteEntry.COLUMN_USERRATING
import com.example.sarada.moviereviews.data.FavoriteContract.FavoriteEntry.TABLE_NAME
import com.example.sarada.moviereviews.data.FavoriteContract.FavoriteEntry._ID

class FavoriteDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    var dbhandler: SQLiteOpenHelper? = null
    override fun close() {
        dbhandler!!.close()
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val SQL_CREATE_FAVORITE_TABLE =
            """CREATE TABLE $TABLE_NAME (
                    $_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_MOVIEID INTEGER,
                    $COLUMN_TITLE TEXT NOT NULL,
                    $COLUMN_USERRATING REAL NOT NULL,
                    $COLUMN_POSTER_PATH TEXT NOT NULL,
                    $COLUMN_PLOT_SYNOPSIS TEXT NOT NULL,
                    $COLUMN_RELEASE_DATE TEXT NOT NULL); """
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(sqLiteDatabase)
    }

    companion object {
        private const val DATABASE_NAME = "favorite.db"
        private const val DATABASE_VERSION = 4
    }
}