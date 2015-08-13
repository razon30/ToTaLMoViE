package com.example.razon30.totalmovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class FragmentSearch extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_MOVIE = "state_genre";
    //    String genreUrl = "http://api.themoviedb.org/3/genre/movie/list?api_key=f246d5e5105e9934d3cd4c4c181d618d";
//
    ArrayList<Movie> trailer_list = new ArrayList<Movie>();
    String trailer = "http://www.myapifilms.com/imdb/trailers";
    String imdb_bottom_100 = "http://www.myapifilms.com/imdb/bottom";
    String imdb_top_250 = "http://www.myapifilms.com/imdb/top";
    String item_of_top_250_of_imdb = "http://api.themoviedb.org/3/find/";
    // String imdb_id;
    String itemPost = "?external_source=imdb_id&api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String urlPre = "http://api.themoviedb.org/3/search/";
    String multiPost = "multi?api_key=f246d5e5105e9934d3cd4c4c181d618d&query=";
    String keyword = "";
    String image_url = "http://image.tmdb.org/t/p/w500";
    String popular_movie = "http://api.themoviedb.org/3/movie/popular?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String popular_person = "http://api.themoviedb.org/3/person/popular?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    ArrayList<String> listmovies = new ArrayList<String>();
    ImageView view1, view2, view3, view4, view5, view6;
    TextView tv1, tv2, tv3, tv_top10, tv_bottom10, tvTrailer, tv_popular_movies, tv_popular_persons,
            tv4, tv5, tv6;
    String id1, id2, id3;
    String pid1, pid2, pid3;
    String tid1, tid2, tid3;
    ImageView trialerImageView1, trialerImageView2, trialerImageView3;
    TextView tvTrailer1, tvTrailer2, tvTrailer3;
    String imdb_id;
    String DB_id = "";
    EditText etSearch;
    Button btnSearch;
    ListView lvGenre1, lvGenre2, lvGenre3;
    String[] genreName1 = {"Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama"};
    String[] genreName2 = {"Family", "Fantasy", "Foreign", "History", "Horror", "Music", "Mystery"};
    String[] genreName3 = {"Romance", "Science Fiction", "Tv Movie", "Thriller", "War", "Western"};
    String[] genreId1 = {"28", "12", "16", "35", "80", "99", "18"};
    String[] genreId2 = {"10751", "14", "10769", "36", "27", "10402", "9648"};
    String[] genreId3 = {"10749", "878", "10770", "53", "10752", "37"};
    Adapter_genre adapter_genre;
    ArrayList<Movie> topten = new ArrayList<Movie>();
    ArrayList<Movie> bottomten = new ArrayList<Movie>();
    ArrayList<Movie> popular_movie_list = new ArrayList<Movie>();
    ArrayList<Movie> popular_person_list = new ArrayList<Movie>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;


//
//    private VolleySingleton volleySingleton;
//    private RequestQueue requestQueue;
//    ArrayList<Movie> genreList = new ArrayList<Movie>();
//    ArrayList<Movie> personList = new ArrayList<Movie>();
//    ArrayList<Movie> movieList = new ArrayList<Movie>();
//    public ArrayList<Movie> listMovies = new ArrayList<Movie>();
//
//    Adapter_genre adapter_genre;

//    private RecyclerView listMovieHits;
//    public ArrayList<Movie> listMovies = new ArrayList<Movie>();
//    Adapter_Genre_Recycle adapter_genre_recycle;

    public static FragmentSearch newInstance(String param1, String param2) {
        FragmentSearch fragment = new FragmentSearch();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_search, container, false);
        initialize(view);

        //   new TaskLoadUpcomingSearching(this,popular_movie).execute();

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();


        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, popular_movie, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(getActivity(), "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                long id = -1;
                                String title = "NA";
                                String releaseDate = "NA";
                                int audienceScore = -1;
                                String synopsis = "NA";
                                String urlThumbnail = "NA";

                                JSONObject currentmovie = jsonArray.getJSONObject(i);
                                title = currentmovie.getString("title");
                                id = currentmovie.getLong("id");
                                releaseDate = currentmovie.getString("release_date");
                                audienceScore = currentmovie.getInt("vote_average");
                                synopsis = currentmovie.getString("overview");
                                urlThumbnail = currentmovie.getString("backdrop_path");


                                Movie movie = new Movie(id, title, releaseDate, audienceScore, synopsis, urlThumbnail);

                                if (id != -1 && !title.equals("NA")) {
                                    popular_movie_list.add(movie);
                                }
                            }


                            String image1 = jsonArray.getJSONObject(0).getString("poster_path");
                            String image2 = jsonArray.getJSONObject(1).getString("poster_path");
                            String image3 = jsonArray.getJSONObject(2).getString("poster_path");


                            String text1 = jsonArray.getJSONObject(0).getString("title");
                            String text2 = jsonArray.getJSONObject(1).getString("title");
                            String text3 = jsonArray.getJSONObject(2).getString("title");

                            id1 = jsonArray.getJSONObject(0).getString("id");
                            id2 = jsonArray.getJSONObject(1).getString("id");
                            id3 = jsonArray.getJSONObject(2).getString("id");


                            Picasso.with(getActivity()).load(image_url + image1).into(view1);
                            Picasso.with(getActivity()).load(image_url + image2).into(view2);
                            Picasso.with(getActivity()).load(image_url + image3).into(view3);

                            tv1.setText(text1);
                            tv2.setText(text2);
                            tv3.setText(text3);

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

        JsonArrayRequest request2 = new JsonArrayRequest(imdb_top_250,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        if (jsonArray == null || jsonArray.length() == 0) {
                            Toast.makeText(getActivity(), "Problem to load", Toast.LENGTH_LONG)
                                    .show();
                        }

                        try {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject current = jsonArray.getJSONObject(i);

                                String imdb_id = current.getString("idIMDB");
                                String title = current.getString("title");
                                String urlPoster = current.getString("urlPoster");
                                int rating = current.getInt("rating");
                                String year = current.getString("year");


//                                JsonObjectRequest request0 = new JsonObjectRequest(Request.Method.GET,
//                                        item_of_top_250_of_imdb+ "tt0478970" +itemPost, null,
//
//                                        new Response.Listener<JSONObject>() {
//                                            @Override
//                                            public void onResponse(JSONObject jsonObject) {
//
//                                                if (jsonObject == null || jsonObject.length() == 0) {
//                                                    Toast.makeText(getActivity(), "Problem to load", Toast
//                                                            .LENGTH_LONG)
//                                                            .show();
//
//                                                }
//
//                                                try {
//
//
//                                                    JSONArray array = jsonObject.getJSONArray("movie_results");
//
//                                                    for (int j=0;j<array.length();j++){
//
//                                                        JSONObject cur = array.getJSONObject(j);
//
//                                                        DB_id = ["movie_results"][0]["id"];
//
//
//                                                    }
//
//
//
//                                                } catch (Exception e) {
////                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
////                                    .show();
//
//
//                                                }
//
//
//                                            }
//                                        },
//                                        new Response.ErrorListener() {
//                                            @Override
//                                            public void onErrorResponse(VolleyError volleyError) {
////                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
////                                .show();
//
//                                            }
//                                        });
//
//                                requestQueue.add(request0);

                                Movie movie = new Movie(imdb_id, title, urlPoster, rating, year);
                                topten.add(movie);

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

        requestQueue.add(request2);

        tv_top10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(getActivity(),topten.toString()+"",Toast.LENGTH_LONG).show();

                View view1 = getActivity().getLayoutInflater().inflate(R.layout
                        .cast_crew_recycler, null);

                Adapter_top_ten_IMDB adapter = new Adapter_top_ten_IMDB(getActivity(), topten);

                RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.cast_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);


                recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(getActivity(),
                        recyclerView, new ClickListener() {
                    @Override
                    public void onCLick(View v, int position) {
                        //  Toast.makeText(getActivity(), "Touched on: " + position, Toast
                        //         .LENGTH_LONG).show();

                        Movie movie = topten.get(position);
                        String imdb_idd = movie.getImdb_id_top();


//                        JsonObjectRequest request0 = new JsonObjectRequest(Request.Method.GET,
//                                item_of_top_250_of_imdb+ imdb_id +itemPost, null,
//
//                                new Response.Listener<JSONObject>() {
//                                    @Override
//                                    public void onResponse(JSONObject jsonObject) {
//
//                                        if (jsonObject == null || jsonObject.length() == 0) {
//                                            Toast.makeText(getActivity(), "Problem to load", Toast
//                                                    .LENGTH_LONG)
//                                                    .show();
//
//                                        }
//
//                                        try {
//
//
//                                            JSONArray array = jsonObject.getJSONArray("movie_results");
//
//                                            JSONObject cur = array.getJSONObject(0);
//
//                                            DB_id = cur.getString("id");
//
//
//                                        } catch (Exception e) {
////                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
////                                    .show();
//
//
//                                        }
//
//
//                                    }
//                                },
//                                new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError volleyError) {
////                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
////                                .show();
//
//                                    }
//                                });
//
//                        requestQueue.add(request0);

                        Intent intent = new Intent(getActivity(), IMDB_Movie_Details_Top_Bottom.class);
                        intent.putExtra("tv", imdb_idd);

//                        Toast.makeText(getActivity(), imdb_idd + "", Toast.LENGTH_LONG)
//                                .show();


                        startActivity(intent);

//                        else {
//
//                            Toast.makeText(getActivity(), DB_id.toString(), Toast.LENGTH_LONG)
//                               .show();
//                        }


                    }

                    @Override
                    public void onLongClick(View v, int position) {

                        Toast.makeText(getActivity(), "Long Touched on: " + position, Toast.LENGTH_LONG)
                                .show();

                    }
                }));


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        getActivity());

                if (topten == null || topten.size() == 0) {
                    Toast.makeText(getActivity(), "No Cast and crew Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();
                }


            }
        });

        //for bottom 10 IMDB

        JsonArrayRequest request3 = new JsonArrayRequest(imdb_bottom_100,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        if (jsonArray == null || jsonArray.length() == 0) {
                            Toast.makeText(getActivity(), "Problem to load", Toast.LENGTH_LONG)
                                    .show();
                        }

                        try {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject current = jsonArray.getJSONObject(i);

                                String imdb_id = current.getString("idIMDB");
                                String title = current.getString("title");
                                String urlPoster = current.getString("urlPoster");
                                int rating = current.getInt("rating");
                                String year = current.getString("year");


//                                JsonObjectRequest request0 = new JsonObjectRequest(Request.Method.GET,
//                                        item_of_top_250_of_imdb+ "tt0478970" +itemPost, null,
//
//                                        new Response.Listener<JSONObject>() {
//                                            @Override
//                                            public void onResponse(JSONObject jsonObject) {
//
//                                                if (jsonObject == null || jsonObject.length() == 0) {
//                                                    Toast.makeText(getActivity(), "Problem to load", Toast
//                                                            .LENGTH_LONG)
//                                                            .show();
//
//                                                }
//
//                                                try {
//
//
//                                                    JSONArray array = jsonObject.getJSONArray("movie_results");
//
//                                                    for (int j=0;j<array.length();j++){
//
//                                                        JSONObject cur = array.getJSONObject(j);
//
//                                                        DB_id = ["movie_results"][0]["id"];
//
//
//                                                    }
//
//
//
//                                                } catch (Exception e) {
////                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
////                                    .show();
//
//
//                                                }
//
//
//                                            }
//                                        },
//                                        new Response.ErrorListener() {
//                                            @Override
//                                            public void onErrorResponse(VolleyError volleyError) {
////                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
////                                .show();
//
//                                            }
//                                        });
//
//                                requestQueue.add(request0);

                                Movie movie = new Movie(imdb_id, title, urlPoster, rating, year);
                                bottomten.add(movie);

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

        requestQueue.add(request3);

        tv_bottom10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(getActivity(),topten.toString()+"",Toast.LENGTH_LONG).show();

                View view1 = getActivity().getLayoutInflater().inflate(R.layout
                        .cast_crew_recycler, null);

                Adapter_top_ten_IMDB adapter = new Adapter_top_ten_IMDB(getActivity(), bottomten);

                RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.cast_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);


                recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(getActivity(),
                        recyclerView, new ClickListener() {
                    @Override
                    public void onCLick(View v, int position) {
                        //  Toast.makeText(getActivity(), "Touched on: " + position, Toast
                        //         .LENGTH_LONG).show();

                        Movie movie = bottomten.get(position);
                        String imdb_idd = movie.getImdb_id_top();


//                        JsonObjectRequest request0 = new JsonObjectRequest(Request.Method.GET,
//                                item_of_top_250_of_imdb+ imdb_id +itemPost, null,
//
//                                new Response.Listener<JSONObject>() {
//                                    @Override
//                                    public void onResponse(JSONObject jsonObject) {
//
//                                        if (jsonObject == null || jsonObject.length() == 0) {
//                                            Toast.makeText(getActivity(), "Problem to load", Toast
//                                                    .LENGTH_LONG)
//                                                    .show();
//
//                                        }
//
//                                        try {
//
//
//                                            JSONArray array = jsonObject.getJSONArray("movie_results");
//
//                                            JSONObject cur = array.getJSONObject(0);
//
//                                            DB_id = cur.getString("id");
//
//
//                                        } catch (Exception e) {
////                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
////                                    .show();
//
//
//                                        }
//
//
//                                    }
//                                },
//                                new Response.ErrorListener() {
//                                    @Override
//                                    public void onErrorResponse(VolleyError volleyError) {
////                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
////                                .show();
//
//                                    }
//                                });
//
//                        requestQueue.add(request0);

                        Intent intent = new Intent(getActivity(), IMDB_Movie_Details_Top_Bottom.class);
                        intent.putExtra("tv", imdb_idd);

//                        Toast.makeText(getActivity(), imdb_idd + "", Toast.LENGTH_LONG)
//                                .show();


                        startActivity(intent);

//                        else {
//
//                            Toast.makeText(getActivity(), DB_id.toString(), Toast.LENGTH_LONG)
//                               .show();
//                        }


                    }

                    @Override
                    public void onLongClick(View v, int position) {

                        //  Toast.makeText(getActivity(), "Long Touched on: " + position, Toast
                        //        .LENGTH_LONG)
                        //        .show();

                    }
                }));


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        getActivity());

                if (bottomten == null || bottomten.size() == 0) {
                    Toast.makeText(getActivity(), "No Cast and crew Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();
                }


            }
        });


        //trailers IMDB

        JsonObjectRequest request4 = new JsonObjectRequest(Request.Method.GET, trailer, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {

                        }

                        try {

                            JSONArray traailer_array = jsonObject.getJSONArray("trailers");

                            JSONObject current_trailer0 = traailer_array.getJSONObject(0);
                            String idIMDB0 = current_trailer0.getString("idIMDB");
                            String url0 = "http://api.themoviedb.org/3/movie/" + idIMDB0 + "?external_source=imdb_id&api_key=f246d5e5105e9934d3cd4c4c181d618d";
                            tid1 = current_trailer0.getString("videoURL");


                            JsonObjectRequest request0 = new JsonObjectRequest(Request.Method
                                    .GET, url0, null,

                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {

                                            if (jsonObject == null || jsonObject.length() == 0) {
                                                Toast.makeText(getActivity(), "Problem to load", Toast.LENGTH_LONG)
                                                        .show();

                                            }

                                            try {


                                                String posterpath = jsonObject.getString("poster_path");
                                                String title = jsonObject.getString("title");
                                                //tid1 = jsonObject.getString("id");

                                                if (posterpath != null && !posterpath.isEmpty() &&
                                                        posterpath.length() != 0) {

                                                    Picasso.with(getActivity()).load
                                                            (image_url + posterpath).into(trialerImageView1);
                                                    tvTrailer1.setText(title);
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

                            requestQueue.add(request0);


                            JSONObject current_trailer1 = traailer_array.getJSONObject(1);
                            String idIMDB1 = current_trailer1.getString("idIMDB");
                            String url1 = "http://api.themoviedb" +
                                    ".org/3/movie/" + idIMDB1 + "?external_source=imdb_id&api_key" +
                                    "=f246d5e5105e9934d3cd4c4c181d618d";
                            tid2 = current_trailer1.getString("videoURL");

                            JsonObjectRequest request1 = new JsonObjectRequest(Request.Method
                                    .GET, url1, null,

                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {

                                            if (jsonObject == null || jsonObject.length() == 0) {
                                                Toast.makeText(getActivity(), "Problem to load", Toast.LENGTH_LONG)
                                                        .show();

                                            }

                                            try {


                                                String posterpath = jsonObject.getString("poster_path");
                                                String title = jsonObject.getString("title");
                                                // tid2 = jsonObject.getString("id");

                                                if (posterpath != null && !posterpath.isEmpty() &&
                                                        posterpath.length() != 0) {

                                                    Picasso.with(getActivity()).load
                                                            (image_url + posterpath).into
                                                            (trialerImageView2);
                                                    tvTrailer2.setText(title);
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


                            JSONObject current_trailer2 = traailer_array.getJSONObject(2);
                            String idIMDB2 = current_trailer2.getString("idIMDB");
                            String url2 = "http://api.themoviedb" +
                                    ".org/3/movie/" + idIMDB2 + "?external_source=imdb_id&api_key" +
                                    "=f246d5e5105e9934d3cd4c4c181d618d";
                            tid3 = current_trailer2.getString("videoURL");

                            JsonObjectRequest request2 = new JsonObjectRequest(Request.Method
                                    .GET, url2, null,

                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject jsonObject) {

                                            if (jsonObject == null || jsonObject.length() == 0) {
                                                Toast.makeText(getActivity(), "Problem to load", Toast.LENGTH_LONG)
                                                        .show();

                                            }

                                            try {


                                                String posterpath = jsonObject.getString("poster_path");
                                                String title = jsonObject.getString("title");
                                                // tid3 = jsonObject.getString("id");

                                                if (posterpath != null && !posterpath.isEmpty() &&
                                                        posterpath.length() != 0) {

                                                    Picasso.with(getActivity()).load
                                                            (image_url + posterpath).into
                                                            (trialerImageView3);
                                                    tvTrailer3.setText(title);
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

                            requestQueue.add(request2);


                            for (int i = 0; i < traailer_array.length(); i++) {

                                JSONObject current_trailer = traailer_array.getJSONObject(i);

                                String duration = current_trailer.getString("duration");
                                String title = current_trailer.getString("title");
                                String url = current_trailer.getString("videoURL");


                                Movie movie = new Movie(title, duration, url);
                                trailer_list.add(movie);

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

        requestQueue.add(request4);

        tvTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view10 = getActivity().getLayoutInflater().inflate(R.layout.fragment_trailer,
                        null);
                ListView listView = (ListView) view10.findViewById(R.id.list_trailer);
                Adapter_trailer adapter = new Adapter_trailer(getActivity());
                adapter.getData(trailer_list);

                listView.setAdapter(adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Movie movie = trailer_list.get(position);
                        String link = movie.getVideoURL();


                        if (link != null && link.length() != 0) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                        } else {
                            return;
                        }

                    }
                });


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        getActivity());

                if (trailer_list == null || trailer_list.size() == 0) {
                    Toast.makeText(getActivity(), trailer_list.toString() + "", Toast.LENGTH_LONG).show();

                    return;
                } else {

                    builderAlertDialog
                            .setView(view10)
                            .show();
                }


            }
        });

        //popular movies

        tv_popular_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view1 = getActivity().getLayoutInflater().inflate(R.layout.cast_crew_recycler, null);

                AdapterBoxOffice castAdapter = new AdapterBoxOffice(getActivity());
                castAdapter.setMovies(popular_movie_list);
                RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.cast_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(castAdapter);

                recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(getActivity(),
                        recyclerView, new ClickListener() {
                    @Override
                    public void onCLick(View v, int position) {
                        Toast.makeText(getActivity(), "Touched on: " + position, Toast.LENGTH_LONG).show();

                        Movie movie = popular_movie_list.get(position);
                        String id = String.valueOf(movie.getId());

                        Intent intent = new Intent(getActivity(), Movie_Details.class);
                        intent.putExtra("tv", id);


                        startActivity(intent);


                    }

                    @Override
                    public void onLongClick(View v, int position) {

                        // Toast.makeText(getActivity(), "Long Touched on: " + position, Toast
                        //        .LENGTH_LONG).show();

                    }
                }));


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        getActivity());

                if (popular_movie_list == null || popular_movie_list.size() == 0) {
                    Toast.makeText(getActivity(), "No more Popular Movie Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();
                }

            }
        });

        //popular person
        JsonObjectRequest request5 = new JsonObjectRequest(Request.Method.GET, popular_person, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(getActivity(), "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject current_result = jsonArray.getJSONObject(i);

                                String name = current_result.getString("name");
                                String profile_path = current_result.getString("profile_path");
                                long id = current_result.getLong("id");
                                String job = current_result.getString("popularity");

                                job = job.substring(0, 4);


                                Movie movie = new Movie(job, profile_path, id, name);
                                popular_person_list.add(movie);

                            }


                            String image1 = jsonArray.getJSONObject(0).getString("profile_path");
                            String image2 = jsonArray.getJSONObject(1).getString("profile_path");
                            String image3 = jsonArray.getJSONObject(2).getString("profile_path");


                            String text1 = jsonArray.getJSONObject(0).getString("name");
                            String text2 = jsonArray.getJSONObject(1).getString("name");
                            String text3 = jsonArray.getJSONObject(2).getString("name");

                            pid1 = jsonArray.getJSONObject(0).getString("id");
                            pid2 = jsonArray.getJSONObject(1).getString("id");
                            pid3 = jsonArray.getJSONObject(2).getString("id");


                            Picasso.with(getActivity()).load(image_url + image1).into(view4);
                            Picasso.with(getActivity()).load(image_url + image2).into(view5);
                            Picasso.with(getActivity()).load(image_url + image3).into(view6);

                            tv4.setText(text1);
                            tv5.setText(text2);
                            tv6.setText(text3);

                        } catch (Exception e) {

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

        requestQueue.add(request5);

        tv_popular_persons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = getActivity().getLayoutInflater().inflate(R.layout.review, null);
                ListView movieView = (ListView) view1.findViewById(R.id.review_list);

                Popular_Person_Adapter adapter = new Popular_Person_Adapter
                        (popular_person_list, getActivity());

                movieView.setAdapter(adapter);

                movieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Movie current = popular_person_list.get(position);
                        String movie_id = String.valueOf(current.getId());

                        Intent intent1 = new Intent(getActivity(), Person_Details.class);
                        intent1.putExtra("tv", movie_id);
                        startActivity(intent1);

                    }
                });


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(getActivity());

                if (popular_person_list == null || popular_person_list.size() == 0) {
                    Toast.makeText(getActivity(), "No Popular Person is Found or Network " +
                                    "Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();

                }


            }
        });


        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Movie_Details.class);
                intent.putExtra("tv", id1);
                startActivity(intent);

            }
        });

        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Movie_Details.class);
                intent.putExtra("tv", id2);
                startActivity(intent);

            }
        });

        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Movie_Details.class);
                intent.putExtra("tv", id3);
                startActivity(intent);

            }
        });

        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Person_Details.class);
                intent.putExtra("tv", pid1);
                startActivity(intent);

            }
        });

        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Person_Details.class);
                intent.putExtra("tv", pid2);
                startActivity(intent);

            }
        });

        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), Person_Details.class);
                intent.putExtra("tv", pid3);
                startActivity(intent);

            }
        });

        trialerImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tid1 != null && tid1.length() != 0) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tid1)));
                } else {
                    return;
                }

            }
        });
        trialerImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tid2 != null && tid2.length() != 0) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tid2)));
                } else {
                    return;
                }
            }
        });
        trialerImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tid3 != null && tid3.length() != 0) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tid3)));
                } else {
                    return;
                }
            }
        });


        adapter_genre = new Adapter_genre(getActivity(), genreName1);
        lvGenre1.setAdapter(adapter_genre);

        lvGenre1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String idd = genreId1[position];

                Intent intent = new Intent(getActivity(), Search_List_Activity.class);
                intent.putExtra("tv", idd);
                startActivity(intent);


            }
        });

        adapter_genre = new Adapter_genre(getActivity(), genreName2);
        lvGenre2.setAdapter(adapter_genre);

        lvGenre2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String idd = genreId2[position];

                Intent intent = new Intent(getActivity(), Search_List_Activity.class);
                intent.putExtra("tv", idd);
                startActivity(intent);


            }
        });

        adapter_genre = new Adapter_genre(getActivity(), genreName3);
        lvGenre3.setAdapter(adapter_genre);

        lvGenre3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String idd = genreId3[position];

                Intent intent = new Intent(getActivity(), Search_List_Activity.class);
                intent.putExtra("tv", idd);
                startActivity(intent);


            }
        });

//        btnSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String keyword = etSearch.getText().toString().trim();
//
//                if (keyword!=null && keyword.length()!=0 && keyword!=""){
//
//                    keyword = keyword.replaceAll("\\s", "");
//
//                    String key = urlPre+multiPost+keyword;
//
//                    Intent intent = new Intent(getActivity(),Multi_Search_Activity.class);
//                    intent.putExtra("tv",key);
//                    startActivity(intent);
//
//                }else {
//                    Toast.makeText(getActivity(),"Not Proper Keyword",Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//            }
//        });


//        volleySingleton = VolleySingleton.getsInstance();
//        requestQueue = volleySingleton.getmRequestQueue();
//        adapter_genre = new Adapter_genre(getActivity());
//        lvGenre.setAdapter(adapter_genre);

//        listMovieHits = (RecyclerView) view.findViewById(R.id.search_genre_list);
//        listMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter_genre_recycle = new Adapter_Genre_Recycle(getActivity());
//        listMovieHits.setAdapter(adapter_genre_recycle);
//
//
//        if (savedInstanceState != null) {
//            listMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIE);
//            adapter_genre_recycle.setMovies(listMovies);
//           // adapter_genre.setMovies(listMovies);
//
//        } else {
//
//            new TaskLoadGenreSearching(this).execute();
//
//
//            // new TaskLoadSearching(getActivity()).execute();
//
//
////            JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, genreUrl, null,
////                    new Response.Listener<JSONObject>() {
////                        @Override
////                        public void onResponse(JSONObject jsonObject) {
////
////                            if (jsonObject == null || jsonObject.length() == 0) {
////                                Toast.makeText(getActivity(), "Problem to load", Toast.LENGTH_LONG)
////                                        .show();
////
////                            }
////
////                            try {
////
////
////                                JSONArray genreArray = jsonObject.getJSONArray("genres");
////
////                                for (int i = 0; i < genreArray.length(); i++) {
////
////                                    JSONObject currentGenre = genreArray.getJSONObject(i);
////
////                                    long id = currentGenre.getLong("id");
////                                    String genre = currentGenre.getString("name");
////
////                                    Movie movie = new Movie(id, genre);
////                                    genreList.add(movie);
////
////                                }
////
////
////                            } catch (Exception e) {
////                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG)
////                                        .show();
////                            }
////                        }
////                    },
////                    new Response.ErrorListener() {
////                        @Override
////                        public void onErrorResponse(VolleyError volleyError) {
////                            Toast.makeText(getActivity(), volleyError.toString(), Toast.LENGTH_LONG)
////                                    .show();
////                        }
////                    });
////
////            requestQueue.add(request1);
//        }
//
//        adapter_genre_recycle.setMovies(listMovies);
//
//        listMovieHits.addOnItemTouchListener(new RecyclerTOuchListener(getActivity(), listMovieHits, new ClickListener() {
//            @Override
//            public void onCLick(View v, int position) {
//                Toast.makeText(getActivity(), "Touched on: " + position, Toast.LENGTH_LONG).show();
//
//                Movie movie = listMovies.get(position);
//                String id = String.valueOf(movie.getGenre_id());
//
//                Intent intent = new Intent(getActivity(), Search_List_Activity.class);
//                intent.putExtra("tv", id);
//
//
//                startActivity(intent);
//
//
//            }
//
//            @Override
//            public void onLongClick(View v, int position) {
//
//                Toast.makeText(getActivity(), "Long Touched on: " + position, Toast.LENGTH_LONG).show();
//
//            }
//        }));
//
//       // adapter_genre.setMovies(listMovies);
//
//
////                    AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
////                            getActivity());
////
////                    if (movieList == null || movieList.size() == 0) {
////                        Toast.makeText(getActivity(), "No Similar Movie Found or Network Error",
////                                Toast.LENGTH_LONG).show();
////                        return;
////                    } else {
////
////                        builderAlertDialog
////                                .setView(view1)
////                                .show();
////
////                        movieList.removeAll(personList);
////                        movieList.removeAll(movieList);
////                        personList.removeAll(personList);
////
////                    }
////
////
////
////                }
//
//
////        lvGenre.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////
////                Movie movie = listMovies.get(position);
////
////               // MyApplication.getWritableDatabase().deleateAll_Searching();
////
////                String idd = String.valueOf(movie.getGenre_id());
////
////                Intent intent = new Intent(getActivity(), Search_List_Activity.class);
////                intent.putExtra("tv", idd);
////                startActivity(intent);
////
////
////            }
////        });

        return view;
    }

    private void initialize(View view) {

//       etSearch = (EditText) view.findViewById(R.id.et_search);
//       btnSearch = (Button) view.findViewById(R.id.btn_search);
        lvGenre1 = (ListView) view.findViewById(R.id.search_genre_list1);
        lvGenre2 = (ListView) view.findViewById(R.id.search_genre_list2);
        lvGenre3 = (ListView) view.findViewById(R.id.search_genre_list3);
        view1 = (ImageView) view.findViewById(R.id.search_fragment_popular_one);
        view2 = (ImageView) view.findViewById(R.id.search_fragment_popular_two);
        view3 = (ImageView) view.findViewById(R.id.search_fragment_popular_three);
        view4 = (ImageView) view.findViewById(R.id.search_fragment_popular_person_one);
        view5 = (ImageView) view.findViewById(R.id.search_fragment_popular_person_two);
        view6 = (ImageView) view.findViewById(R.id.search_fragment_popular_person_three);
        trialerImageView1 = (ImageView) view.findViewById(R.id.search_fragment_trailer_one);
        trialerImageView2 = (ImageView) view.findViewById(R.id.search_fragment_trailer_two);
        trialerImageView3 = (ImageView) view.findViewById(R.id.search_fragment_trailer_three);


        tv1 = (TextView) view.findViewById(R.id.text_search_one);
        tv2 = (TextView) view.findViewById(R.id.text_search_two);
        tv3 = (TextView) view.findViewById(R.id.text_search_three);
        tv4 = (TextView) view.findViewById(R.id.text_search_popular_person_one);
        tv5 = (TextView) view.findViewById(R.id.text_search_popular_person_two);
        tv6 = (TextView) view.findViewById(R.id.text_search_popular_person_three);


        tv_top10 = (TextView) view.findViewById(R.id.top_ten);
        tv_bottom10 = (TextView) view.findViewById(R.id.bottom_ten);
        tvTrailer = (TextView) view.findViewById(R.id.trailer_imdb);
        tv_popular_movies = (TextView) view.findViewById(R.id.see_more_popular_movies);
        tv_popular_persons = (TextView) view.findViewById(R.id.box_office_imdb);
        tvTrailer1 = (TextView) view.findViewById(R.id.text_trailer_one);
        tvTrailer2 = (TextView) view.findViewById(R.id.text_trailer_two);
        tvTrailer3 = (TextView) view.findViewById(R.id.text_trailer_three);


    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList(STATE_MOVIE, listMovies);
//    }
//
//    @Override
//    public void onGenreLoaded(ArrayList<Movie> listMovies) {
//
//        adapter_genre_recycle.setMovies(listMovies);
//
//       // this.listMovies = listMovies;
//
//        //adapter_genre.setMovies(listMovies);
//
//    }

//    public static interface ClickListener{
//
//        public void onCLick(View v, int position);
//        public void onLongClick(View v, int position);
//
//    }
//
//    static class RecyclerTOuchListener implements RecyclerView.OnItemTouchListener{
//
//        GestureDetector gestureDetector;
//        ClickListener clickListener;
//
//        public  RecyclerTOuchListener(Context context, final RecyclerView rv, final ClickListener clickListener){
//
//            this.clickListener = clickListener;
//            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
//
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return  true;
//                }
//
//                @Override
//                public void onLongPress(MotionEvent e) {
//
//                    View child = rv.findChildViewUnder(e.getX(),e.getY());
//                    if (child != null && clickListener !=null){
//                        clickListener.onLongClick(child,rv.getChildPosition(child));
//                    }
//
//                }
//            });
//
//
//        }
//
//
//
//        @Override
//        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//
//            View child = rv.findChildViewUnder(e.getX(),e.getY());
//            if (child !=null && clickListener!=null && gestureDetector.onTouchEvent(e)){
//
//                clickListener.onCLick(child,rv.getChildPosition(child));
//
//            }
//
//            return false;
//        }
//
//        @Override
//        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
//
//        }
//    }


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
    }


}
