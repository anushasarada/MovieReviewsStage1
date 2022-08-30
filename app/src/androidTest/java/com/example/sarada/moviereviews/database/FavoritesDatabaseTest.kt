package com.example.sarada.moviereviews.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesDatabaseTest: TestCase() {

    private lateinit var db: FavoritesDatabase
    private lateinit var dao: FavoritesDatabaseDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, FavoritesDatabase::class.java).build()
        dao = db.favoritesDatabaseDao
    }

    @After
    public override fun tearDown() {
        db.close()
    }

    //@Ignore("This test will be ignored")
    @Test
    fun writeAndReadFavoriteMovie() {
        val favMovie = Favorite(
            5757567579,
            "favorite",
            "1",
            "Narnia",
            "4",
            "wwwabc",
            "A movie about kids & closet jungle",
            "April 2020"
        )
        dao.insert(favMovie)
        val dbFavorites = dao.getAllFavorites().value
        assertThat(dbFavorites?.contains(favMovie)).isTrue()
    }
}