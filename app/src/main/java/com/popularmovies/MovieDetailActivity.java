package com.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

/**
 * Created by aditya on 12/26/16.
 */

public class MovieDetailActivity extends AppCompatActivity {


    private Context thisContext;
    private int pos;

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

        getReviews();

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

        // Add or Remove favorites
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //inserting favorite movie
                ContentValues cv = new ContentValues();

                cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieId);
                cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME, movieName);

                Uri uri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, cv);

                if(uri != null)
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();

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

        String url = thisContext.getString(R.string.moviedb_url)
                +MovieGridAdapter.movies.get(pos).getMovieID()
                +thisContext.getString(R.string.reviews_url)
                +thisContext.getString(R.string.MOVIE_DB_API_KEY);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
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




 /*   public static class MovieDetailFragment extends Fragment {

        public MovieDetailFragment(){
            setHasOptionsMenu(true);
        }

       @Nullable
       @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

           View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

           return view;
       }
   }*/
}
