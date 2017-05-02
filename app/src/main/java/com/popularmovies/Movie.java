package com.popularmovies;

import android.widget.ImageView;

/**
 * Created by aditya on 12/25/16.
 */

public class Movie {
    private String id;
    private String posterPath;
    private String title;
    private String overview;
    private String releaseDate;
    private ImageView image;
    private String rating;

    /**
     * Movie Constructor
     * @param releaseDate
     * @param overview
     * @param id
     * @param posterPath
     * @param title
     * @param rating
     */
    public Movie(String releaseDate, String overview, String id, String posterPath, String title, String rating) {
        this.overview = overview;
        this.id = id;
        this.posterPath = posterPath;
        this.title = title;
        this.releaseDate = releaseDate;
        this.image = null;
        this.rating = rating;
    }

    public String getPosterPath(){
        return this.posterPath;
    }

    public void setImageBitmap(ImageView bp){
        this.image = bp;
    }

    public ImageView getImageBitmap(){
        return this.image;
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

    public String getRating(){
        return this.rating;
    }
}
