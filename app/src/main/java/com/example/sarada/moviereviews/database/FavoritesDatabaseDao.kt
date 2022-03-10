package com.example.sarada.moviereviews.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoritesDatabaseDao {

    @Insert
    fun insert(favorite: Favorite)

    @Update
    fun update(favorite: Favorite)

    @Query("""DELETE FROM FAVORITES_TABLE""")
    fun clear()

    @Query("""SELECT * FROM FAVORITES_TABLE""")
    fun getAllFavorites(): LiveData<List<Favorite>>
}