package com.example.sarada.moviereviews;

import com.example.sarada.moviereviews.models.MovieApiResponse;
import com.example.sarada.moviereviews.models.ReviewResponse;
import com.example.sarada.moviereviews.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sarada on 3/11/2018.
 */

public interface RetrofitQuery {

    @GET("movie/popular")
    Call<MovieApiResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieApiResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("movie_id") int id, @Query("api_key") String apiKey);

}
