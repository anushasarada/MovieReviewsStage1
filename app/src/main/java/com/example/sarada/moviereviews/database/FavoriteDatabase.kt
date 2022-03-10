package com.example.sarada.moviereviews.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class], version = 1, exportSchema = true)
abstract class FavoritesDatabase: RoomDatabase() {

    abstract val favoritesDatabaseDao: FavoritesDatabaseDao

    companion object{

        @Volatile
        private var FAVORITES_DATABASE_INSTANCE: FavoritesDatabase? = null

        fun getInstance(context: Context): FavoritesDatabase{
            synchronized(this){

                var instance = FAVORITES_DATABASE_INSTANCE

                if(instance == null){

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoritesDatabase::class.java,
                        "favorites_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    FAVORITES_DATABASE_INSTANCE = instance
                }

                return instance
            }
        }
    }
}