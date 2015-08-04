package com.example.razon30.totalmovie;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * Created by razon30 on 24-07-15.
 */
public class TaskLoadUpcomingSearching extends AsyncTask<Void,Void,ArrayList<String> > {


    private SearchingUpcomingLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    String id;

    public TaskLoadUpcomingSearching(SearchingUpcomingLoadedListener myComponent, String id) {

        this.myComponent = myComponent;
        this.id = id;
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
    }


    @Override
    protected ArrayList<String> doInBackground(Void... params) {

        ArrayList<String> listMovies = MultiUtils_Upcoming.loadBoxSearch(requestQueue,id);
        return listMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<String > listMovies) {
        if (myComponent != null) {
            myComponent.onSearchingUpcomingLoaded(listMovies);
        }
    }



}
