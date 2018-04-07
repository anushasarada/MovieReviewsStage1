package com.example.sarada.moviereviews.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sarada on 3/11/2018.
 */

public class MovieApiResponse implements android.os.Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeTypedList(this.results);
        dest.writeInt(this.totalResults);
        dest.writeInt(this.totalPages);
    }

    public MovieApiResponse() {
    }

    protected MovieApiResponse(Parcel in) {
        this.page = in.readInt();
        this.results = in.createTypedArrayList(MovieDetails.CREATOR);
        this.totalResults = in.readInt();
        this.totalPages = in.readInt();
    }

    public static final Creator<MovieApiResponse> CREATOR = new Creator<MovieApiResponse>() {
        @Override
        public MovieApiResponse createFromParcel(Parcel source) {
            return new MovieApiResponse(source);
        }

        @Override
        public MovieApiResponse[] newArray(int size) {
            return new MovieApiResponse[size];
        }
    };
}
