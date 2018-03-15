package com.example.sarada.moviereviews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sarada on 3/11/2018.
 */

public interface RetrofitQuery {

    @GET("movie/popular")
    Call<MovieApiResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieApiResponse> getTopRatedMovies(@Query("api_key") String apiKey);

}
