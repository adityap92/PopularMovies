package com.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.popularmovies.data.FavoritesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aditya on 12/26/16.
 */

public class MovieDetailActivity extends AppCompatActivity {


    private Context thisContext;
    private int pos;
    private ArrayList<Trailer> trailers;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        thisContext=getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        pos = extras.getInt("position");
        final String movieId = MovieGridAdapter.movies.get(pos).getMovieID();
        final String movieName = MovieGridAdapter.movies.get(pos).getTitle();

        //pull reviews and trailers
        trailers = new ArrayList<Trailer>();

        getReviews();

        //create listview for trailers
        ListView trailerVideos = (ListView) findViewById(R.id.lvTrailers);
        adapter = new ArrayAdapter<String>(this, R.layout.trailer_list_view, R.id.tvTrailer, new ArrayList<String>());
        trailerVideos.setAdapter(adapter);

        trailerVideos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        getTrailers();


        trailerVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(R.string.youtube_link +
                        trailers.get(position).getTrailerKey())));
            }
        });

        //create UI objects
        ImageView image = (ImageView) findViewById(R.id.detailPoster);
        TextView title = (TextView) findViewById(R.id.tvDetailTitle);
        TextView releaseDate = (TextView) findViewById(R.id.tvDetailYear);
        TextView synopsis = (TextView) findViewById(R.id.tvDetailDescription);
        TextView rating = (TextView) findViewById(R.id.tvDetailRating);
        Button favorite = (Button) findViewById(R.id.bFavorite);

        //Populate MovieDetail references
        image.setImageDrawable(MovieGridAdapter.movies.get(pos).getImageBitmap().getDrawable());
        title.setText(movieName);
        releaseDate.setText((MovieGridAdapter.movies.get(pos).getReleaseDate()).substring(0,4));
        synopsis.setText((MovieGridAdapter.movies.get(pos).getOverview()));
        rating.setText(MovieGridAdapter.movies.get(pos).getRating());

        if(MovieGridAdapter.movies.get(pos).isFave()){
            favorite.setText(R.string.remove_fave);
        }

        // Add or Remove favorites
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(MovieGridAdapter.movies.get(pos).isFave()){
                    getContentResolver().delete(FavoritesContract.FavoritesEntry.CONTENT_URI,
                            movieName,
                            null);
                    MovieGridAdapter.movies.get(pos).setFave(false);
                    Toast.makeText(getBaseContext(), R.string.fave_del_toast, Toast.LENGTH_SHORT).show();
                }else{
                    //inserting favorite movie
                    ContentValues cv = new ContentValues();

                    cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieId);
                    cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME, movieName);

                    Uri uri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, cv);

                    if(uri != null)
                        Toast.makeText(getBaseContext(), R.string.fave_add_toast, Toast.LENGTH_SHORT).show();

                    MovieGridAdapter.movies.get(pos).setFave(true);
                }

                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getReviews(){
        //create '/movie/{id}/reviews' url
        String url = thisContext.getString(R.string.moviedb_url)
                +MovieGridAdapter.movies.get(pos).getMovieID()
                +thisContext.getString(R.string.reviews_url)
                +thisContext.getString(R.string.MOVIE_DB_API_KEY);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    //get Review and Display it
                    JSONArray arr = response.getJSONArray("results");
                    JSONObject review = arr.getJSONObject(0);
                    TextView tvReview = (TextView) findViewById(R.id.tvReview);
                    tvReview.setText(review.getString("content"));

                }catch(JSONException e){
                    Log.e("JSON Exception", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY ERROR",error.toString());
            }

        });

        MovieDBConnection.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private void getTrailers(){

        String url = thisContext.getString(R.string.moviedb_url)
                + MovieGridAdapter.movies.get(pos).getMovieID()
                + thisContext.getString(R.string.trailer_url)
                + thisContext.getString(R.string.MOVIE_DB_API_KEY);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray arr = response.getJSONArray("results");
                    for(int i=0; i < arr.length(); i++){
                        JSONObject obj = arr.getJSONObject(i);
                        trailers.add(new Trailer(obj.getString("name"), obj.getString("key")));
                        adapter.add(obj.getString("name"));
                    }
                    adapter.notifyDataSetChanged();
                }
                catch(JSONException e){
                    Log.e("JSON Exception", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY ERROR",error.toString());
            }
        });

        MovieDBConnection.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private class Trailer{

        String title;
        String key;

        public Trailer(String title, String key){
            this.title = title;
            this.key = key;
        }

        public String getTrailerKey(){
            return key;
        }
    }
}
