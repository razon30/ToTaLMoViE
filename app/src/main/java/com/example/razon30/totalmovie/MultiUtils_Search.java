package com.example.razon30.totalmovie;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 22-07-15.
 */
public class MultiUtils_Search {

    public static ArrayList<Movie> loadBoxSearch(RequestQueue requestQueue, String id) {
        JSONObject response1 = Requestor.sendRequestBoxOfficeMovies(requestQueue,id );

        ArrayList<Movie> listMovies1 = Parser_multi.parseJSONResponse(response1);

        return listMovies1;
    }


}
