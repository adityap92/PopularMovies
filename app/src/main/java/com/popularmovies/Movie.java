package com.popularmovies;

/**
 * Created by aditya on 12/25/16.
 */

public class Movie {
    private String id;
    private String posterPath;
    private String title;
    private String overview;


    public Movie(String overview, String id, String posterPath, String title) {
        this.overview = overview;
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
    }

    public String getPosterPath(){
        return this.posterPath;
    }
}
