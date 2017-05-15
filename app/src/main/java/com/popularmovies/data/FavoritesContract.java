package com.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by Aditya on 5/15/2017.
 */

public class FavoritesContract {

    public static final class FavoritesEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_NAME = "movieName";
    }
}
