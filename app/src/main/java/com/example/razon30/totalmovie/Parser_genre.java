package com.example.razon30.totalmovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 22-07-15.
 */
public class Parser_genre {

    public static ArrayList<Movie> parseJSONResponse(JSONObject response) {
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Movie> listMovies = new ArrayList<Movie>();
        if (response != null && response.length() > 0) {
            try {


                JSONArray genreArray = response.getJSONArray("genres");

                                for (int i = 0; i < genreArray.length(); i++) {

                                    JSONObject currentGenre = genreArray.getJSONObject(i);

                                    long id = currentGenre.getLong("id");
                                    String genre = currentGenre.getString("name");

                                    Movie movie = new Movie(id, genre);
                                    listMovies.add(movie);

                                }

            } catch (JSONException e) {

            }

        }
        return listMovies;
    }

}
