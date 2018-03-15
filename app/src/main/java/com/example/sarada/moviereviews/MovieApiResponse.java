package com.example.sarada.moviereviews;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sarada on 3/11/2018.
 */

public class MovieApiResponse {

    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<MovieDetails> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieDetails> getResults() {
        return results;
    }

    public List<MovieDetails> getMovies() {
        return results;
    }

    public void setResults(List<MovieDetails> results) {
        this.results = results;
    }

    public void setMovies(List<MovieDetails> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
