package com.example.sarada.moviereviews;

/**
 * Created by sarada on 3/11/2018.
 */

public class MovieModel {

    String movieName, releaseDate, ImageURI, rating;

    public MovieModel() {

    }

    public MovieModel(String movieName, String releaseDate, String ImageURI, String rating) {

        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.ImageURI = ImageURI;
        this.rating = rating;

    }
}
