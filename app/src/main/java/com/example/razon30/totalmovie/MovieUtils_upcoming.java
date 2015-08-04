package com.example.razon30.totalmovie;

import com.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 19-07-15.
 */
public class MovieUtils_upcoming {

    static String URL1 = "http://api.themoviedb" +
            ".org/3/movie/upcoming?api_key=f246d5e5105e9934d3cd4c4c181d618d&append_to_response" +
            "=images&include_image_language=en,null";

    static String URL2 = "http://api.themoviedb" +
            ".org/3/movie/upcoming?api_key=f246d5e5105e9934d3cd4c4c181d618d&append_to_response" +
            "=images&include_image_language=en,null&page=2";

    static String URL3 = "http://api.themoviedb" +
            ".org/3/movie/upcoming?api_key=f246d5e5105e9934d3cd4c4c181d618d&append_to_response" +
            "=images&include_image_language=en,null&page=3";

//    static String URL4 = "http://api.themoviedb" +
//            ".org/3/movie/upcoming?api_key=f246d5e5105e9934d3cd4c4c181d618d&append_to_response" +
//            "=images&include_image_language=en,null&page=4";
//
//    static String URL5 = "http://api.themoviedb" +
//            ".org/3/movie/upcoming?api_key=f246d5e5105e9934d3cd4c4c181d618d&append_to_response" +
//            "=images&include_image_language=en,null&page=5";



    public static ArrayList<Movie> loadBoxUpcoming(RequestQueue requestQueue) {
        JSONObject response1 = Requestor.sendRequestBoxOfficeMovies(requestQueue,URL1 );
        JSONObject response2 = Requestor.sendRequestBoxOfficeMovies(requestQueue,URL2 );
        JSONObject response3 = Requestor.sendRequestBoxOfficeMovies(requestQueue,URL3 );
//        JSONObject response4 = Requestor.sendRequestBoxOfficeMovies(requestQueue,URL4 );
//        JSONObject response5 = Requestor.sendRequestBoxOfficeMovies(requestQueue,URL5 );

        ArrayList<Movie> listMovies1 = Parser.parseJSONResponse(response1);
        ArrayList<Movie> listMovies2 = Parser.parseJSONResponse(response2);
        ArrayList<Movie> listMovies3 = Parser.parseJSONResponse(response3);
//        ArrayList<Movie> listMovies4 = Parser.parseJSONResponse(response4);
//        ArrayList<Movie> listMovies5 = Parser.parseJSONResponse(response5);

        //listMovies1.remove(listMovies2);
        listMovies1.addAll(listMovies2);
        listMovies1.addAll(listMovies3);
//        listMovies1.addAll(listMovies4);
//        listMovies1.addAll(listMovies5);

        MyApplication.getWritableDatabase().insertdata_Upcoming(listMovies1, true);
        return listMovies1;
    }

}
