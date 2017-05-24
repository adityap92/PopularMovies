package com.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Aditya on 5/15/2017.
 */

public class FavoritesContract {

    //Constants for ContentProvider
    public static final String AUTHORITY = "com.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_NAME = "movieName";
    }
}
