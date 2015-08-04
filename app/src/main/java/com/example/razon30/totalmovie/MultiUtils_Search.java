package com.example.razon30.totalmovie;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 22-07-15.
 */
public class MultiUtils_Search {

    static   String urlPre = "http://api.themoviedb.org/3/search/";
    static  String multiPost = "multi?api_key=f246d5e5105e9934d3cd4c4c181d618d&query=";


    public static ArrayList<Movie> loadBoxSearch(RequestQueue requestQueue, String id) {
        JSONObject response1 = Requestor.sendRequestBoxOfficeMovies(requestQueue,id );

        ArrayList<Movie> listMovies1 = Parser_multi.parseJSONResponse(response1);

        return listMovies1;
    }


}
