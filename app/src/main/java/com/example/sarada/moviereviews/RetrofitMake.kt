package com.example.sarada.moviereviews

import retrofit2.Retrofit
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Created by sarada on 3/11/2018.
 */

private const val BASE_URL = "https://api.themoviedb.org/3/"

private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val client: OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(interceptor)
}.build()

private var gson = GsonBuilder()
    .setLenient()
    .create()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(client)
    .build()


object MoviesApi {
    val retrofitService: RetrofitQuery by lazy {
        retrofit.create(RetrofitQuery::class.java)
    }
}

/*
private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private val client : OkHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(interceptor)
}.build()

private var gson = GsonBuilder()
    .setLenient()
    .create()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(client)
    .build()*/
