package com.example.razon30.totalmovie;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 22-07-15.
 */
public class GenreUtils {
    static String URL1 = "http://api.themoviedb.org/3/genre/movie/list?api_key=f246d5e5105e9934d3cd4c4c181d618d";


    public static ArrayList<Movie> loadBoxOfficeGenre(RequestQueue requestQueue) {
        JSONObject response1 = Requestor_Genre.sendRequestBoxOfficeGenre(requestQueue, URL1);

        ArrayList<Movie> listMovies1 = Parser_genre.parseJSONResponse(response1);

        return listMovies1;
    }
}
