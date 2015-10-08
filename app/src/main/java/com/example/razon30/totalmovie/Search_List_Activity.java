package com.example.razon30.totalmovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.RequestQueue;

import java.util.ArrayList;


public class Search_List_Activity extends AppCompatActivity implements SearchingMoviesLoadedListener{

    private static final String STATE_MOVIE = "state_movie_search_list";
    public ArrayList<Movie> listMovies = new ArrayList<Movie>();
    String urlPreId = "http://api.themoviedb.org/3/genre/";
    String urlPostId = "/movies?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String id;
    String image_url = "http://image.tmdb.org/t/p/w500";
    //recycle
    private RecyclerView listMovieHits;
    private AdapterBoxOffice adapterBoxOffice;
    //VOlley-Json
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        Intent intent = getIntent();
        id = intent.getStringExtra("tv");

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();

        listMovieHits = (RecyclerView) findViewById(R.id.search_recycler);
        listMovieHits.setLayoutManager(new LinearLayoutManager(Search_List_Activity.this));
        adapterBoxOffice = new AdapterBoxOffice(Search_List_Activity.this);
        listMovieHits.setAdapter(adapterBoxOffice);

        if (savedInstanceState != null) {
            listMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIE);
            adapterBoxOffice.setMovies(listMovies);
        } else {
          // listMovies = MyApplication.getWritableDatabase().getAllMoviesSearching();
           // if (listMovies.isEmpty()) {
                // L.t(getActivity(), "executing task from fragment");
                new TaskLoadMoviesSearching(Search_List_Activity.this,id).execute();
         //  }

        }

//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
//                    urlPreId + id + urlPostId, null,
//
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject jsonObject) {
//
//                            if (jsonObject == null || jsonObject.length() == 0) {
//                                Toast.makeText(Search_List_Activity.this, "Problem to load", Toast.LENGTH_LONG)
//                                        .show();
//
//                            }
//
//                            try {
//
//                                JSONArray arrayMovies = jsonObject.getJSONArray("results");
//                                for (int i = 0; i < arrayMovies.length(); i++) {
//                                    long id = -1;
//                                    String title = "NA";
//                                    String releaseDate = "NA";
//                                    int audienceScore = -1;
//                                    String synopsis = "NA";
//                                    String urlThumbnail = "NA";
//
//                                    JSONObject currentmovie = arrayMovies.getJSONObject(i);
//                                    title = currentmovie.getString("title");
//                                    id = currentmovie.getLong("id");
//                                    releaseDate = currentmovie.getString("release_date");
//                                    audienceScore = currentmovie.getInt("vote_average");
//                                    synopsis = currentmovie.getString("overview");
//                                    urlThumbnail = currentmovie.getString("poster_path");
//
//
//                                    Movie movie = new Movie(id,title,releaseDate,audienceScore,synopsis,urlThumbnail);
//
//                                    if (id != -1 && !title.equals("NA")) {
//                                        listMovies.add(movie);
//                                    }
//                                }
//
//
//                            } catch (Exception e) {
//                                Toast.makeText(Search_List_Activity.this, e.toString(), Toast.LENGTH_LONG)
//                                        .show();
//
//
//                            }
//
//
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(Search_List_Activity.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//                    });
//
//            requestQueue.add(request);
//        }

        adapterBoxOffice.setMovies(listMovies);
//        Toast.makeText(Search_List_Activity.this, listMovies.size()+"", Toast.LENGTH_LONG)
//                .show();

        listMovieHits.addOnItemTouchListener(new RecyclerTOuchListener(Search_List_Activity.this,
                listMovieHits, new
                ClickListener() {
            @Override
            public void onCLick(View v, int position) {
                //Toast.makeText(Search_List_Activity.this, "Touched on: " + position, Toast
                       // .LENGTH_LONG).show();

                Movie movie = listMovies.get(position);
                String id = String.valueOf(movie.getId());
                String image = image_url + movie.getUrlThumbnail();

                Intent intent = new Intent(Search_List_Activity.this, Movie_Details.class);
                intent.putExtra("tv", id);
                intent.putExtra("url", image);


                startActivity(intent);


            }

            @Override
            public void onLongClick(View v, int position) {

               // Toast.makeText(Search_List_Activity.this, "Long Touched on: " + position, Toast
                     //   .LENGTH_LONG).show();

            }
        }));


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIE, listMovies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search__list_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchingMoviesLoaded(ArrayList<Movie> listMovies) {
        adapterBoxOffice.setMovies(listMovies);

        this.listMovies = listMovies;
    }


    public interface ClickListener {

        void onCLick(View v, int position);

        void onLongClick(View v, int position);

    }

    static class RecyclerTOuchListener implements RecyclerView.OnItemTouchListener{

        GestureDetector gestureDetector;
        ClickListener clickListener;

        public  RecyclerTOuchListener(Context context, final RecyclerView rv, final ClickListener clickListener){

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return  true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = rv.findChildViewUnder(e.getX(),e.getY());
                    if (child != null && clickListener !=null){
                        clickListener.onLongClick(child,rv.getChildPosition(child));
                    }

                }
            });


        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(),e.getY());
            if (child !=null && clickListener!=null && gestureDetector.onTouchEvent(e)){

                clickListener.onCLick(child,rv.getChildPosition(child));

            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }



}
