package com.example.razon30.totalmovie;

import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by razon30 on 27-07-15.
 */
public class TrailerLoadTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    private UpcomingMoviesLoadedListener myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    String trailer = "http://www.myapifilms.com/imdb/trailers";


    public TrailerLoadTask(UpcomingMoviesLoadedListener myComponent) {

        this.myComponent = myComponent;
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();
    }


    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {

        final ArrayList<Movie> listMovies = new ArrayList<Movie>();


        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, trailer, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {

                        }

                        try {

                            JSONArray traailer_array = jsonObject.getJSONArray("trailers");
                            for (int i = 0; i < traailer_array.length(); i++) {

                                JSONObject current_trailer = traailer_array.getJSONObject(i);

                                String duration = current_trailer.getString("duration");
                                String title = current_trailer.getString("title");
                                String url = current_trailer.getString("videoURL");


                                Movie movie = new Movie(title, duration, url);
                                listMovies.add(movie);

                            }


                        } catch (Exception e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

        requestQueue.add(request1);


        return listMovies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> listMovies) {
        if (myComponent != null) {
            myComponent.onUpcomingMoviesLoaded(listMovies);
        }
    }


}
