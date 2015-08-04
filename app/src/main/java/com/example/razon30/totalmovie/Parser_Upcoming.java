package com.example.razon30.totalmovie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 24-07-15.
 */
public class Parser_Upcoming {

    public static ArrayList<String> parseJSONResponse(JSONObject response) {
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<String> listMovies = new ArrayList<String>();
        if (response != null && response.length() > 0) {
            try {

                JSONArray resultArray = response.getJSONArray("results");

                for (int i = 0; i < resultArray.length(); i++) {

                    JSONObject current_result = resultArray.getJSONObject(i);

                    String image = current_result.getString("backdrop_path");

                    listMovies.add(image);

                }


            } catch (JSONException e) {

            }

        }
        return listMovies;
    }


}
