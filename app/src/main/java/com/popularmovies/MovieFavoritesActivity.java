package com.popularmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.popularmovies.data.FavoritesContract;

import java.util.ArrayList;

/**
 * Class to load Favorite Movies from SQLite DB
 *
 * Created by aditya on 6/15/17.
 */

public class MovieFavoritesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = MovieFavoritesActivity.class.getSimpleName();
    private static final int FAV_LOADER_ID = 0;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_favorites);

        ListView faves = (ListView) findViewById(R.id.lvFavorites);
        adapter = new ArrayAdapter<String>(this, R.layout.favorites_list_view, R.id.tvFave, new ArrayList<String>());
        faves.setAdapter(adapter);

        getSupportLoaderManager().initLoader(FAV_LOADER_ID,null,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        getSupportLoaderManager().restartLoader(FAV_LOADER_ID,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    //load previous data
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try{
                    return getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                }catch(Exception e){
                    Log.e(TAG,"Failed to load data Asynchronously");
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()){
            do{
                adapter.add(data.getString(data.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME)));
            }while(data.moveToNext());

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }



}
