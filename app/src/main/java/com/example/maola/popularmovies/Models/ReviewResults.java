package com.example.maola.popularmovies.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by maola on 22/04/18.
 */

public class ReviewResults {
    @SerializedName("results")
    @Expose
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }

}
