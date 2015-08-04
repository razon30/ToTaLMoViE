package com.example.razon30.totalmovie;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 24-07-15.
 */
public class MultiUtils_Upcoming {

    public static ArrayList<String> loadBoxSearch(RequestQueue requestQueue, String id) {
        JSONObject response1 = Requestor.sendRequestBoxOfficeMovies(requestQueue,id );

        ArrayList<String> listMovies1 = Parser_Upcoming.parseJSONResponse(response1);

        return listMovies1;
    }



}
