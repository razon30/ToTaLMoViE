package com.example.razon30.totalmovie;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * Created by razon30 on 19-07-15.
 */
public class TaskLoadMoviesUpcoming extends AsyncTask<Void,Void,ArrayList<Movie> > {

    private UpcomingMoviesLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;


    public TaskLoadMoviesUpcoming(UpcomingMoviesLoadedListener myComponent) {

        this.myComponent = myComponent;
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
    }


    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        ArrayList<Movie> listMovies = MovieUtils_upcoming.loadBoxUpcoming(requestQueue);
        return listMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> listMovies) {
        if (myComponent != null) {
            myComponent.onUpcomingMoviesLoaded(listMovies);
        }
    }


}
