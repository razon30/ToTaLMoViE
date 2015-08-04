package com.example.razon30.totalmovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 22-07-15.
 */
public class Parser_multi {

    public static ArrayList<Movie> parseJSONResponse(JSONObject response) {
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Movie> listMovies = new ArrayList<Movie>();
        if (response != null && response.length() > 0) {
            try {

                JSONArray resultArray = response.getJSONArray("results");

                for (int i=0;i<resultArray.length();i++){

                    JSONObject current_result = resultArray.getJSONObject(i);

                    String type = current_result.getString("media_type");

                    if ("person".equalsIgnoreCase(type)){

                        long id = current_result.getLong("id");
                        String name = current_result.getString("name");
                        String profile = current_result.getString("profile_path");

                        Movie movie = new Movie(profile,id,name,type);
                        listMovies.add(movie);

                    }else if ("movie".equalsIgnoreCase(type)){

                        long id = current_result.getLong("id");
                        String name = current_result.getString("title");
                        String profile = current_result.getString("poster_path");

                        Movie movie = new Movie(profile,id,name,type);
                        listMovies.add(movie);

                    }



                }



            } catch (JSONException e) {

            }

        }
        return listMovies;
    }


}
