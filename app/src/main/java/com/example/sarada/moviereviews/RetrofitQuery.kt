package com.example.sarada.moviereviews

import retrofit2.http.GET
import com.example.sarada.moviereviews.models.datac.MovieApiResponse
import com.example.sarada.moviereviews.models.datac.TrailerResponse
import com.example.sarada.moviereviews.models.datac.ReviewResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by sarada on 3/11/2018.
 */
interface RetrofitQuery {
    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String?): Deferred<MovieApiResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String?): Deferred<MovieApiResponse>

    @GET("movie/{movie_id}/videos")
    fun getMovieTrailer(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String?
    ): Deferred<TrailerResponse>

    @GET("movie/{movie_id}/reviews")
    fun getMovieReviews(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String?
    ): Deferred<ReviewResponse>
}