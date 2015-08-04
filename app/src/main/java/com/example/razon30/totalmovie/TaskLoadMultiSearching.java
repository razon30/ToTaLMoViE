package com.example.razon30.totalmovie;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * Created by razon30 on 22-07-15.
 */
public class TaskLoadMultiSearching extends AsyncTask<Void,Void,ArrayList<Movie> > {

    private SearchingMultiLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    String id;

    public TaskLoadMultiSearching(SearchingMultiLoadedListener myComponent, String id) {

        this.myComponent = myComponent;
        this.id = id;
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
    }


    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        ArrayList<Movie> listMovies = MultiUtils_Search.loadBoxSearch(requestQueue,id);
        return listMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> listMovies) {
        if (myComponent != null) {
            myComponent.onSearchingMultiLoaded(listMovies);
        }
    }



}
