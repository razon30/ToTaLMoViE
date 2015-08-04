package com.example.razon30.totalmovie;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 22-07-15.
 */
public class MovieUtils_Search {

    static  String urlPreId = "http://api.themoviedb.org/3/genre/";

    static   String urlPostId1 = "/movies?api_key=f246d5e5105e9934d3cd4c4c181d618d&append_to_response" +
            "=images&include_image_language=en,null";
    static   String urlPostId2 = "/movies?api_key=f246d5e5105e9934d3cd4c4c181d618d&append_to_response" +
            "=images&include_image_language=en,null&page=2";


    public static ArrayList<Movie> loadBoxSearch(RequestQueue requestQueue, String id) {
        JSONObject response1 = Requestor.sendRequestBoxOfficeMovies(requestQueue,urlPreId+id+urlPostId1 );
        JSONObject response2 = Requestor.sendRequestBoxOfficeMovies(requestQueue,
                urlPreId+id+urlPostId2 );

        ArrayList<Movie> listMovies1 = Parser.parseJSONResponse(response1);
        ArrayList<Movie> listMovies2 = Parser.parseJSONResponse(response2);

        listMovies1.addAll(listMovies2);

        MyApplication.getWritableDatabase().insertdata_Searching(listMovies1, true);
        return listMovies1;
    }

}
