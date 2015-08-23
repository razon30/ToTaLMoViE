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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;


public class Multi_Search_Activity extends AppCompatActivity implements SearchingMultiLoadedListener{

    private static final String STATE_MOVIE = "multi_search_list";
    public ArrayList<Movie> listMovies = new ArrayList<Movie>();
    String urlPre = "http://api.themoviedb.org/3/search/";
    String multiPost = "multi?api_key=f246d5e5105e9934d3cd4c4c181d618d&query=";
    Adapter_KKeyword_Search adapter_kKeyword_search;
    TextView tv;
    //recycle
    private RecyclerView listMovieHits;
    //VOlley-Json
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_search);
        tv= (TextView) findViewById(R.id.tv_multi);

        Intent intent = getIntent();
        String keyword = intent.getStringExtra("tv");

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();

        listMovieHits = (RecyclerView) findViewById(R.id.multi_recycler);
        listMovieHits.setLayoutManager(new LinearLayoutManager(Multi_Search_Activity.this));
        adapter_kKeyword_search = new Adapter_KKeyword_Search(Multi_Search_Activity.this);
        listMovieHits.setAdapter(adapter_kKeyword_search);

        if (savedInstanceState != null) {
            listMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIE);
            adapter_kKeyword_search.setMovies(listMovies);
        } else {
            // listMovies = MyApplication.getWritableDatabase().getAllMoviesSearching();
            // if (listMovies.isEmpty()) {
            // L.t(getActivity(), "executing task from fragment");
            new TaskLoadMultiSearching(Multi_Search_Activity.this,keyword).execute();
            //  }

        }


        adapter_kKeyword_search.setMovies(listMovies);
        Toast.makeText(Multi_Search_Activity.this, listMovies.size() + "", Toast.LENGTH_LONG)
                .show();

        listMovieHits.addOnItemTouchListener(new RecyclerTOuchListener(Multi_Search_Activity.this,
                listMovieHits, new
                ClickListener() {
                    @Override
                    public void onCLick(View v, int position) {
                      //  Toast.makeText(Multi_Search_Activity.this, "Touched on: " + position,
                            //    Toast.LENGTH_LONG).show();

                        Movie movie = listMovies.get(position);

                        String type = movie.getType();

                        if ("person".equalsIgnoreCase(type)){

                            String id = String.valueOf(movie.getId());
                            Intent intent1 = new Intent(Multi_Search_Activity.this,Person_Details
                                    .class);
                            intent1.putExtra("tv",id);
                            startActivity(intent1);

                        }else if ("movie".equalsIgnoreCase(type)){

                            String id = String.valueOf(movie.getId());
                            Intent intent1 = new Intent(Multi_Search_Activity.this,Movie_Details
                                    .class);
                            intent1.putExtra("tv",id);
                            startActivity(intent1);

                        }


                    }

                    @Override
                    public void onLongClick(View v, int position) {

                       // Toast.makeText(Multi_Search_Activity.this, "Long Touched on: " +
                               // position, Toast.LENGTH_LONG).show();

                    }
                }));






    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIE, listMovies);
    }


    @Override
    public void onSearchingMultiLoaded(ArrayList<Movie> listMovies) {
       adapter_kKeyword_search.setMovies(listMovies);

        if (listMovies==null || listMovies.size()==0){

            listMovieHits.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("No Person Or Movie Found Or No Network Access");

        }else {

            listMovieHits.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
            this.listMovies = listMovies;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi__search_, menu);
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
