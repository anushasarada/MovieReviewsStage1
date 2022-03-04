package com.example.sarada.moviereviews

import retrofit2.Retrofit
import com.example.sarada.moviereviews.RetrofitMake
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by sarada on 3/11/2018.
 */
object RetrofitMake {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private var retrofit: Retrofit? = null
    @JvmStatic
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}