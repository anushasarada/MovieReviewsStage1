package com.example.sarada.moviereviews.database

import android.content.ContentProvider
import com.example.sarada.moviereviews.database.FavoriteContract.FavoriteEntry
import android.content.ContentValues
import android.content.ContentUris
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import java.lang.UnsupportedOperationException

class FavoriteContentProvider : ContentProvider() {

    private var favoriteDbHelper: FavoriteDbHelper? = null

    override fun onCreate(): Boolean {
        val context = context
        favoriteDbHelper = FavoriteDbHelper(context)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val db = favoriteDbHelper!!.readableDatabase
        val retCursor: Cursor = when (sUriMatcher.match(uri)) {
            FAVORITE -> db.query(
                FavoriteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            else -> throw UnsupportedOperationException("Unknown uri:$uri")
        }
        retCursor.setNotificationUri(context!!.contentResolver, uri)
        return retCursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val db = favoriteDbHelper!!.writableDatabase
        val match = sUriMatcher.match(uri)
        var returnUri: Uri? = null
        when (match) {
            FAVORITE -> {
                val id = db.insert(FavoriteEntry.TABLE_NAME, null, contentValues)
                returnUri = if (id > 0) {
                    ContentUris.withAppendedId(FavoriteEntry.CONTENT_URI, id)
                } else {
                    throw SQLException("Failed to insert row into $uri")
                }
            }
            FAVORITE_WITH_ID -> {}
            else -> throw UnsupportedOperationException("Unknown uri:$uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return returnUri
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val db = favoriteDbHelper!!.writableDatabase
        val favsDeleted: Int = when (sUriMatcher.match(uri)) {
            FAVORITE_WITH_ID -> {
                val id = uri.pathSegments[1]
                db.delete(FavoriteEntry.TABLE_NAME, "movieid=?", arrayOf(id))
            }
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }
        if (favsDeleted != 0) {
            context!!.contentResolver.notifyChange(uri, null)
        }
        return favsDeleted
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?
    ): Int {
        return 0
    }

    companion object {
        const val FAVORITE = 100
        const val FAVORITE_WITH_ID = 101
        private val sUriMatcher = buildUriMatcher()
        private fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(FavoriteContract.AUTHORITY, FavoriteContract.PATH_FAVORITE, FAVORITE)
            uriMatcher.addURI(
                FavoriteContract.AUTHORITY,
                FavoriteContract.PATH_FAVORITE + "/#",
                FAVORITE_WITH_ID
            )
            return uriMatcher
        }
    }
}