package com.example.razon30.totalmovie;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBoxOffice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBoxOffice extends android.support.v4.app.Fragment implements Sort,
        BoxOfficeMoviesLoadedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_MOVIE = "state_movie";
    public ArrayList<Movie> listMovies = new ArrayList<Movie>();
    //sorting
    public MovieSorter movieSorter = new MovieSorter();
    Toolbar toolbar;
    String urlPreId = "http://api.themoviedb.org/3/movie/";
    long id;
    String urlLaterId = "?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String image_url = "http://image.tmdb.org/t/p/w500";
    String vediopost = "/videos?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String cast_post = "/credits?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String image_post = "/images?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String similar_post = "/similar?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String reviews_post = "/reviews?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //recycle
    private RecyclerView listMovieHits;
    private AdapterBoxOffice adapterBoxOffice;
    //VOlley-Json
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static FragmentBoxOffice newInstance(String param1, String param2) {
        FragmentBoxOffice fragment = new FragmentBoxOffice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_box_office, container, false);

      //  swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);

       // swipeRefreshLayout.setOnRefreshListener(this);

        workingOnFAB(view);

        listMovieHits = (RecyclerView) view.findViewById(R.id.box_office);
        listMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        listMovieHits.setAdapter(adapterBoxOffice);

        if (savedInstanceState!=null){
            listMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIE);
            adapterBoxOffice.setMovies(listMovies);
        }
        else {

            listMovies = MyApplication.getWritableDatabase().getAllMoviesBoxOffice();
            if (listMovies.isEmpty()) {
                // L.t(getActivity(), "executing task from fragment");
                new TaskLoadMoviesBoxOffice(this).execute();
            }

        }
        adapterBoxOffice.setMovies(listMovies);


        listMovieHits.addOnItemTouchListener(new RecyclerTOuchListener(getActivity(), listMovieHits, new ClickListener() {
            @Override
            public void onCLick(View v, int position) {
                //  Toast.makeText(getActivity(), "Touched on: " + position, Toast.LENGTH_LONG)
                // .show();

                Movie movie = listMovies.get(position);
                String id = String.valueOf(movie.getId());

                Intent intent = new Intent(getActivity(), Movie_Details.class);
                intent.putExtra("tv", id);


                startActivity(intent);


            }

            @Override
            public void onLongClick(View v, int position) {

                //  Toast.makeText(getActivity(), "Long Touched on: " + position, Toast.LENGTH_LONG)
                //     .show();

            }
        }));



        return view;
    }

    private void workingOnFAB(final View view) {
        FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) view.findViewById(R.id
                .multiple_actions);

       // menuMultipleActions.setBackgroundResource(R.drawable.refresh);

        com.getbase.floatingactionbutton.FloatingActionButton action_a = (com.getbase
                .floatingactionbutton.FloatingActionButton) view.findViewById(R.id.action_a);
        final RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout);

        action_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Snackbar.make(layout,"Sorting Alphabetically",Snackbar.LENGTH_SHORT).show();
                sortByName();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton action_b = (com.getbase
                .floatingactionbutton.FloatingActionButton) view.findViewById(R.id.action_b);
        action_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                Snackbar.make(layout,"Sorting By Date",Snackbar.LENGTH_SHORT).show();
                sortByDate();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton action_c = (com.getbase
                .floatingactionbutton.FloatingActionButton) view.findViewById(R.id.action_c);
        action_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(layout, "Sorting By Ratings", Snackbar.LENGTH_SHORT)
                        .show();
                sortByRatings();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIE, listMovies);
    }

    @Override
    public void sortByName() {

        // Toast.makeText(getActivity(), "sort name box office", Toast.LENGTH_LONG).show();
        movieSorter.sortMoviesByName(listMovies);
        adapterBoxOffice.notifyDataSetChanged();

    }

    @Override
    public void sortByDate() {

        // Toast.makeText(getActivity(), "sort date box office", Toast.LENGTH_LONG).show();
        movieSorter.sortMovieByDate(listMovies);
        adapterBoxOffice.notifyDataSetChanged();

    }

    @Override
    public void sortByRatings() {

        // Toast.makeText(getActivity(), "sort rating box office", Toast.LENGTH_LONG).show();
        movieSorter.sortMoviesByRating(listMovies);
        adapterBoxOffice.notifyDataSetChanged();

    }

    @Override
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> listMovies) {

//        if (swipeRefreshLayout.isRefreshing())
//        {
//            swipeRefreshLayout.setRefreshing(false);
//            adapterBoxOffice.notifyDataSetChanged();
//        }

        adapterBoxOffice.setMovies(listMovies);
        adapterBoxOffice.notifyDataSetChanged();

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



    }


}
