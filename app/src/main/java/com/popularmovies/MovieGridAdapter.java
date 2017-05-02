package com.popularmovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aditya on 12/25/16.
 *
 * This class is to download the movie images and display them in the GridView.
 */

public class MovieGridAdapter extends BaseAdapter {

    private Context thisContext;
    public static ArrayList<Movie> movies;
    private String currFilter;

    public MovieGridAdapter(Context c){
        thisContext = c;
        movies = new ArrayList<Movie>();
        currFilter = thisContext.getResources().getString(R.string.most_popular_url);

        getImageUrls();

    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(thisContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        } else {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);
        String url = thisContext.getString(R.string.get_image)
                +(movies.get(position).getPosterPath())
                +thisContext.getString(R.string.api_url)
                +thisContext.getString(R.string.MOVIE_DB_API_KEY);

        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);

                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Image Retrieve Error: ",error.toString());
                    }
                });
        // Access the RequestQueue through your singleton class.
        MovieDBConnection.getInstance(thisContext).addToRequestQueue(request);

        movies.get(position).setImageBitmap(imageView);
        return imageView;
    }

    public void setFilter(String newFilter){
        currFilter = newFilter;
        getImageUrls();
    }

    public ArrayList<Movie> getMovies(){
        return movies;
    }

    private void getImageUrls(){
        String url="";
        if(currFilter.equals(thisContext.getResources().getString(R.string.most_popular_url))){
            url =  thisContext.getString(R.string.most_popular_url)+thisContext.getString(R.string.MOVIE_DB_API_KEY);
        }else if(currFilter.equals(thisContext.getResources().getString(R.string.top_rated_url))) {
            url =  thisContext.getString(R.string.top_rated_url)+thisContext.getString(R.string.MOVIE_DB_API_KEY);
        }


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {


                        try{
                        JSONArray arr = response.getJSONArray("results");
                        movies.clear();

                        for(int i = 0; i < 20 ; i++){

                            JSONObject currMovie = arr.getJSONObject(i);
                            movies.add(new Movie(currMovie.getString("release_date"),currMovie.getString("overview"),
                                        currMovie.getString("id"),
                                        currMovie.getString("poster_path"),
                                        currMovie.getString("title"),
                                        currMovie.getString("vote_average")));

                        }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("asdf",error.toString());
                    }
                });

        // Access the RequestQueue through your singleton class.
        MovieDBConnection.getInstance(thisContext).addToRequestQueue(jsObjRequest);
    }
}
