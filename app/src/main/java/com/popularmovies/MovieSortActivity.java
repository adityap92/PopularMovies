package com.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MovieSortActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);


        GridView gridview = (GridView) findViewById(R.id.movieGrid);
        gridview.setAdapter(new MovieGridAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MovieSortActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        setContentView(R.layout.activity_movie_sort);
    }
}
