package com.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by aditya on 12/26/16.
 */

public class MovieDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        int pos = extras.getInt("position");

        //create UI objects
        ImageView image = (ImageView) findViewById(R.id.detailPoster);
        TextView title = (TextView) findViewById(R.id.tvDetailTitle);
        TextView releaseDate = (TextView) findViewById(R.id.tvDetailYear);
        TextView synopsis = (TextView) findViewById(R.id.tvDetailDescription);
        TextView rating = (TextView) findViewById(R.id.tvDetailRating);

        //Populate MovieDetail references
        image.setImageDrawable(MovieGridAdapter.movies.get(pos).getImageBitmap().getDrawable());
        title.setText((MovieGridAdapter.movies.get(pos).getTitle()));
        releaseDate.setText((MovieGridAdapter.movies.get(pos).getReleaseDate()).substring(0,4));
        synopsis.setText((MovieGridAdapter.movies.get(pos).getOverview()));
        rating.setText(MovieGridAdapter.movies.get(pos).getRating());

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
