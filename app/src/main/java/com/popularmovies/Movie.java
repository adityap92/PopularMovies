package com.popularmovies;

/**
 * Created by aditya on 12/25/16.
 */

public class Movie {
    private String id;
    private String posterPath;
    private String title;
    private String overview;
    private String releaseDate;


    public Movie(String releaseDate, String overview, String id, String posterPath, String title) {
        this.overview = overview;
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.releaseDate = releaseDate;
    }

    public String getPosterPath(){
        return this.posterPath;
    }

    public String getTitle(){
        return this.title;
    }

    public String getOverview(){
        return this.overview;
    }

    public String getReleaseDate(){
        return this.releaseDate;
    }
}
