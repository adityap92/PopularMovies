package com.popularmovies;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * Created by aditya on 12/25/16.
 * Creates Request to pull from MovieDB API
 */

public class MovieDBConnection {

    private static MovieDBConnection mInstance;
    private RequestQueue mRequestQueue;
    private static Context thisContext;


    public MovieDBConnection(Context context){
        thisContext=context;
        mRequestQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(thisContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static synchronized MovieDBConnection getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MovieDBConnection(context);
        }
        return mInstance;
    }
}
