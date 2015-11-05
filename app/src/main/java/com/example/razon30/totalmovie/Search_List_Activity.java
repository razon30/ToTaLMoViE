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
                new TaskLoadMoviesSearching(Search_List_Activity.this,id).execute();

        }


        adapterBoxOffice.setMovies(listMovies);


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
