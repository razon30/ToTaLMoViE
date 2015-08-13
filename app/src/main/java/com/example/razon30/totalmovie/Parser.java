package com.example.razon30.totalmovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 11-07-15.
 */
public class Parser {

    public static ArrayList<Movie> parseJSONResponse(JSONObject response) {
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<Movie> listMovies = new ArrayList<Movie>();
        if (response != null && response.length() > 0) {
            try {
                JSONArray arrayMovies = response.getJSONArray("results");
                for (int i = 0; i < arrayMovies.length(); i++) {
                    long id = -1;
                    String title = "NA";
                    String releaseDate = "NA";
                    int audienceScore = -1;
                    String synopsis = "NA";
                    String urlThumbnail = "NA";

                    JSONObject currentmovie = arrayMovies.getJSONObject(i);
                    title = currentmovie.getString("title");
                    id = currentmovie.getLong("id");
                    releaseDate = currentmovie.getString("release_date");
                    audienceScore = currentmovie.getInt("vote_average");
                    synopsis = currentmovie.getString("overview");
                    urlThumbnail = currentmovie.getString("backdrop_path");


                    Movie movie = new Movie(id,title,releaseDate,audienceScore,synopsis,urlThumbnail);

                if (id != -1 && !title.equals("NA")) {
                        listMovies.add(movie);
                    }
                }

            } catch (JSONException e) {

            }

        }
        return listMovies;
    }

}
