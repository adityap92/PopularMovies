package com.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MovieSortActivity extends Activity {
    private Context mainContext;
    private MovieGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_sort);

        mainContext=getApplicationContext();

        MovieDBConnection mdbconnect = new MovieDBConnection(this);


        GridView gridview = (GridView) findViewById(R.id.movieGrid);
        gridAdapter = new MovieGridAdapter(this);
        gridview.setAdapter(gridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MovieSortActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.filter_popular){
            gridAdapter.setFilter(mainContext.getResources().getString(R.string.most_popular_url));
            //gridAdapter.notifyDataSetChanged();
            return true;
        }else
        if(id==R.id.filter_rated){
            gridAdapter.setFilter(mainContext.getResources().getString(R.string.top_rated_url));
            //gridAdapter.notifyDataSetChanged();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.grid_setting, menu);


        return true;
    }
}
