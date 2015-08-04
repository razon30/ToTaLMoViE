package com.example.razon30.totalmovie;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * Created by razon30 on 22-07-15.
 */
public class TaskLoadGenreSearching extends AsyncTask<Void,Void,ArrayList<Movie> > {

    private GenreLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;


    public TaskLoadGenreSearching(GenreLoadedListener myComponent) {

        this.myComponent = myComponent;
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
    }


    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        ArrayList<Movie> listMovies = GenreUtils.loadBoxOfficeGenre(requestQueue);
        return listMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> listMovies) {
        if (myComponent != null) {
            myComponent.onGenreLoaded(listMovies);
        }
    }


}
