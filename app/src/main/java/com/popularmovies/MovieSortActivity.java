package com.popularmovies;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class MovieSortActivity extends AppCompatActivity {
    private Context mainContext;
    private MovieGridAdapter gridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_sort);
        mainContext=getApplicationContext();

        //initialize UI objects
        GridView gridview = (GridView) findViewById(R.id.movieGrid);
        gridAdapter = new MovieGridAdapter(this);
        gridview.setAdapter(gridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                //open MovieDetail Screen
                Intent newIntent = new Intent(mainContext, MovieDetailActivity.class);
                newIntent.putExtra("position", position);

                startActivity(newIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.filter_popular:
                gridAdapter.setFilter(mainContext.getResources().getString(R.string.most_popular_url));
                return true;
            case R.id.filter_rated:
                gridAdapter.setFilter(mainContext.getResources().getString(R.string.top_rated_url));
                return true;
            case R.id.favorite_menu:
                startActivity(new Intent(mainContext, MovieFavoritesActivity.class));
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

    public static class MovieSortFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_movie_sort, container, false);

            return view;
        }
    }
}
