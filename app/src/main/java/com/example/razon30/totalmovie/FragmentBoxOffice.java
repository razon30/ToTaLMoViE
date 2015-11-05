package com.example.razon30.totalmovie;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.mrengineer13.snackbar.SnackBar;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBoxOffice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBoxOffice extends android.support.v4.app.Fragment implements Sort,
        BoxOfficeMoviesLoadedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_MOVIE = "state_movie";
    public ArrayList<Movie> listMovies = new ArrayList<Movie>();
    //sorting
    public MovieSorter movieSorter = new MovieSorter();
    //VOlley-Json
    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    long id;

    String image_url = "http://image.tmdb.org/t/p/w500";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //recycle
    private RecyclerView listMovieHits;
    private AdapterBoxOffice adapterBoxOffice;
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


        if (!isNetworkAvailable()) {

            if (savedInstanceState != null) {
                listMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIE);
                adapterBoxOffice.setMovies(listMovies);
            } else {

                listMovies = MyApplication.getWritableDatabase().getAllMoviesBoxOffice();

            }

        } else {
            new TaskLoadMoviesBoxOffice_now().execute();
            adapterBoxOffice.setMovies(listMovies);
            adapterBoxOffice.notifyDataSetChanged();
        }




        listMovieHits.addOnItemTouchListener(new RecyclerTOuchListener(getActivity(), listMovieHits, new ClickListener() {
            @Override
            public void onCLick(View v, int position) {

                Movie movie = listMovies.get(position);
                String id = String.valueOf(movie.getId());
                String image = image_url + movie.getUrlThumbnail();

                Intent intent = new Intent(getActivity(), Movie_Details.class);

                intent.putExtra("tv", id);
                intent.putExtra("url", image);
                startActivity(intent);


            }

            @Override
            public void onLongClick(View v, int position) {

            }
        }));


        return view;
    }

    private void workingOnFAB(final View view) {
        FloatingActionMenu menuMultipleActions = (FloatingActionMenu) view.findViewById(R.id
                .multiple_actions);

        FloatingActionButton action_a = (FloatingActionButton) view.findViewById(R.id.action_a);
        final RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.layout);

        action_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SnackBar.Builder(getActivity())
                        .withMessage("Sorting Alphabetically") // OR
                        .withTextColorId(R.color.translucent_black_light)
                        .withBackgroundColorId(R.color.accent_color)
                        .withTypeFace(Typeface.SANS_SERIF)
                        .show();
                sortByName();

            }
        });

        FloatingActionButton action_b = (FloatingActionButton) view.findViewById(R.id.action_b);
        action_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                new SnackBar.Builder(getActivity())
                        .withMessage("Sorting By Date") // OR
                        .withTextColorId(R.color.translucent_black_light)
                        .withBackgroundColorId(R.color.accent_color)
                        .withTypeFace(Typeface.SANS_SERIF)
                        .show();
                sortByDate();
            }
        });

        FloatingActionButton action_c = (FloatingActionButton) view.findViewById(R.id.action_c);
        action_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SnackBar.Builder(getActivity())
                        .withMessage("Sorting By Ratings") // OR
                        .withTextColorId(R.color.translucent_black_light)
                        .withBackgroundColorId(R.color.accent_color)
                        .withTypeFace(Typeface.SANS_SERIF)
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

        // adapterBoxOffice.setMovies(listMovies);
        adapterBoxOffice.notifyDataSetChanged();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface ClickListener {

        void onCLick(View v, int position);

        void onLongClick(View v, int position);

    }

    static class RecyclerTOuchListener implements RecyclerView.OnItemTouchListener {

        GestureDetector gestureDetector;
        ClickListener clickListener;

        public RecyclerTOuchListener(Context context, final RecyclerView rv, final ClickListener clickListener) {

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, rv.getChildPosition(child));
                    }

                }
            });


        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {

                clickListener.onCLick(child, rv.getChildPosition(child));

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

    public class TaskLoadMoviesBoxOffice_now extends AsyncTask<Void, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            volleySingleton = VolleySingleton.getsInstance();
            requestQueue = volleySingleton.getmRequestQueue();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            listMovies = MovieUtils.loadBoxOfficeMovies(requestQueue);
            return listMovies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> aVoid) {
            super.onPostExecute(aVoid);

            adapterBoxOffice.setMovies(listMovies);
            adapterBoxOffice.notifyDataSetChanged();


        }
    }

}
