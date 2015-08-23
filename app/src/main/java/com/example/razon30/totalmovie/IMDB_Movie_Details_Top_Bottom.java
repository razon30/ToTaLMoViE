package com.example.razon30.totalmovie;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class IMDB_Movie_Details_Top_Bottom extends AppCompatActivity {

    public ArrayList<Movie> cast_and_crew = new ArrayList<Movie>();
    //similar
    public ArrayList<Movie> similar_list = new ArrayList<Movie>();
    //reviews
    public ArrayList<Movie> reviews = new ArrayList<Movie>();
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout rootLayout;
    String w_id;
    String w_name;
    int a = 1;
    ImageView watch;
    ImageView wish, add;
    DBMovies dbMovies;
    LinearLayout layout1, layout2, layout3;
    ImageView coverLayout, coverLayout1;
    ImageView circularImageView, circularImageView1;
    ImageView imageView, imageRating;
    TextView button, btn_moreImage, btn_reviws, btn_similar;
    TextView tvGenre, tvOverview, tvHomepage, tvProduction, tvGenreDown, tvRevenue, tvTagLine,
            tvImbdId, tvRating;
    String urlPreId = "http://api.themoviedb.org/3/movie/";
    long id;
    String item_of_top_250_of_imdb = "http://api.themoviedb.org/3/movie/";
    String imdb_id;
    String itemPost = "?external_source=imdb_id&api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String urlLaterId = "?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String image_url = "http://image.tmdb.org/t/p/w500";
    String vediopost = "/videos?external_source=imdb_id&api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String cast_post = "/credits?external_source=imdb_id&api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String image_post = "/images?external_source=imdb_id&api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String similar_post = "/similar?external_source=imdb_id&api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String reviews_post = "/reviews?external_source=imdb_id&api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String trailer, homepage;
    long current_id;
    ArrayList<String> movie_id = new ArrayList<String>();
    //more image
    ListView oddList, evenList;
    ImageView more_image;
    ArrayList<String> oddArray = new ArrayList<String>();
    ArrayList<String> evenArray = new ArrayList<String>();
    ArrayList<String> more_image_array = new ArrayList<String>();
    private ImageLoader imageLoader;
    //Retriving data
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imdb__movie__details__top__bottom);
        Intent intent = getIntent();
        imdb_id = intent.getStringExtra("tv");
        w_id = String.valueOf(id);
        initualizing_contents();

        watch = (ImageView) findViewById(R.id.watch);
        wish = (ImageView) findViewById(R.id.wish);
        dbMovies = new DBMovies(IMDB_Movie_Details_Top_Bottom.this);
        layout1 = (LinearLayout) findViewById(R.id.multiple_layout);
        layout2 = (LinearLayout) findViewById(R.id.watch_layout);
        layout3 = (LinearLayout) findViewById(R.id.wish_layout);
        boolean bool1 = dbMovies.checkWatch(w_id);
        if (bool1) {

            watch.setBackgroundResource(R.color.accent_color);

        }
        boolean bool2 = dbMovies.checkWish(w_id);
        if (bool2) {

            wish.setBackgroundResource(R.color.primary_color_dark);

        }

        if (bool1 | bool2) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
        } else {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
            layout3.setVisibility(View.GONE);
        }

        add = (ImageView) findViewById(R.id.multiple_actions);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
            }
        });


        //  scrollView = (ScrollView) findViewById(R.id.movie_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);



        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();

//        JsonObjectRequest request0 = new JsonObjectRequest(Request.Method.GET,
//                item_of_top_250_of_imdb+id+itemPost, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        if (jsonObject == null || jsonObject.length() == 0) {
//                            Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Problem to load", Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//
//                        try {
//
//
//                            id = jsonObject.getLong("id");
//
//
//                        } catch (Exception e) {
////                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
////                                    .show();
//
//
//                        }
//
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
////                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
////                                .show();
//
//                    }
//                });
//
//        requestQueue.add(request0);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                item_of_top_250_of_imdb + imdb_id + itemPost, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            String backdrop_path = jsonObject.getString("backdrop_path");

                            if (backdrop_path != "" && backdrop_path != null) {

                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + backdrop_path).into
                                        (coverLayout);

                            } else {
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(R.drawable.ic_launcher).into
                                        (coverLayout);
                                //  coverLayout.setImageResource(R.drawable.ic_launcher);


                            }


                         String  mango_id = String.valueOf(jsonObject.getLong("id"));

                            movie_id.add(mango_id);

                            JSONArray genr = jsonObject.getJSONArray("genres");
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < genr.length(); i++) {
                                JSONObject g = genr.getJSONObject(i);
                                sb.append(g.getString("name"));
                                if (i < genr.length() - 1) {
                                    sb.append(",");
                                }
                            }
                            tvGenre.setText(sb);
                            tvGenreDown.setText(sb);

                            homepage = "";
                            homepage = jsonObject.getString("homepage");
                            if (homepage != "" && homepage != null) {
                                tvHomepage.setText("Home Page:  " + homepage);
                            } else {
                                tvHomepage.setText("Home Page:  " + "NA");
                            }

                            String imdb_id = "";
                            imdb_id = jsonObject.getString("imdb_id");
                            if (imdb_id != "" && imdb_id != null) {
                                tvImbdId.setText(imdb_id);
                            } else {
                                tvImbdId.setText("NA");
                            }

                            String overview = "";
                            overview = jsonObject.getString("overview");
                            if (overview != "" && overview != null) {
                                tvOverview.setText("OVERVIEW:  " + overview);
                            } else {
                                tvOverview.setText("OVERVIEW:  " + "NA");
                            }


                            String poster_path = jsonObject.getString("poster_path");
                            if (poster_path != "" && poster_path != null) {

                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + poster_path).into
                                        (imageView);

                            } else {
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(R.drawable.ic_launcher).into
                                        (imageView);
                                imageView.setImageResource(R.drawable.ic_launcher);


                            }
                            JSONArray produc = jsonObject.getJSONArray("production_companies");
                            StringBuilder production = new StringBuilder();
                            for (int i = 0; i < produc.length(); i++) {

                                JSONObject p = produc.getJSONObject(i);
                                production.append(p.getString("name"));
                                if (i < produc.length() - 1) {
                                    production.append("\n");
                                }

                            }
                            tvProduction.setText(production);

                            String release_Date = jsonObject.getString("release_date");
                            String[] date = release_Date.split("-");
                            String title = jsonObject.getString("title");
                            w_name = title + "  (" + date[0] + ")";
                            // tvTitle.setText(w_name);
                            collapsingToolbarLayout.setTitle(title + "  (" + date[0] + ")");

                            String revenue = "";
                            revenue = jsonObject.getString("revenue");

                            if (revenue != null && revenue.length() != 0 && revenue != "" && revenue
                                    != "0") {
                                tvRevenue.setText(revenue);
                            } else {
                                tvRevenue.setText("Still Running, NO total Revenue");
                            }


                            String tagLine = "";

                            tagLine = jsonObject.getString("tagline");
                            if (tagLine != null && tagLine.length() != 0 && tagLine != "") {
                                tvTagLine.setText(tagLine);
                            } else {
                                tvTagLine.setText("NA");
                            }

                            double audience_score = -1;

                            audience_score = jsonObject.getDouble("vote_average");

                            if (audience_score == -1) {
//                                ratingBar.setRating(0.0F);
//                                ratingBar.setAlpha(0.5F);

                                tvRating.setText(audience_score + "");

                            } else {
//                                ratingBar.setRating(audience_score / 2.0F);
//                                ratingBar.setAlpha(1.0F);

                                tvRating.setText(audience_score + "");

                            }

//                            Movie movie = new Movie(title,release_Date,audience_score,
//                                    backdrop_path,genres,overview,poster_path,revenue,tagLine,
//                                    imdb_id,homepage,production);
//
//                            listMovieDetails.add(movie);

//                            JSONArray movie = jsonObject.getJSONArray("results");
//                            for (int i = 0; i < movie.length(); i++) {
//                                JSONObject currentmovie = movie.getJSONObject(i);
//                                String name = currentmovie.getString("title");
//                                long id = currentmovie.getLong("id");
//                                String release_date = currentmovie.getString("release_date");
//                                int audience_score = -1;
//                                audience_score = currentmovie.getInt("vote_average");
//                                String synopsis = currentmovie.getString("overview");
//                                String urlthumbnile = currentmovie.getString("poster_path");
//
//                                Movie movie1 = new Movie(id, name, release_date, audience_score, synopsis, urlthumbnile);
//                                listMovieDetails.add(movie1);
//
//                            }
                            // Toast.makeText(getActivity(), listMovies.toString(), Toast.LENGTH_LONG).show();


                        } catch (Exception e) {
//                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request);



        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET,
                urlPreId + imdb_id + vediopost, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray ved = jsonObject.getJSONArray("results");

                            JSONObject v = ved.getJSONObject(0);
                            trailer = v.getString("key");

                        } catch (Exception e) {
//                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request1);

        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET,
                urlPreId + imdb_id + cast_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray cast = jsonObject.getJSONArray("cast");
                            for (int i = 0; i < cast.length(); i++) {

                                JSONObject current_cast = cast.getJSONObject(i);
                                String job = current_cast.getString("character");
                                String name = current_cast.getString("name");
                                String profile_thumbnail = current_cast.getString("profile_path");
                                long id = current_cast.getLong("id");

                                Movie movie = new Movie(name, job, profile_thumbnail, id);
                                cast_and_crew.add(movie);

                            }

                            JSONArray crew = jsonObject.getJSONArray("crew");
                            for (int i = 0; i < crew.length(); i++) {

                                JSONObject current_cast = crew.getJSONObject(i);
                                String job = current_cast.getString("job");
                                String name = current_cast.getString("name");
                                String profile_thumbnail = current_cast.getString("profile_path");
                                long id = current_cast.getLong("id");

                                Movie movie = new Movie(name, job, profile_thumbnail, id);
                                cast_and_crew.add(movie);

                            }


                        } catch (Exception e) {
//                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request2);

        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET,
                urlPreId + imdb_id + image_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray image = jsonObject.getJSONArray("backdrops");
                            for (int i = 0; i < image.length(); i++) {

                                JSONObject obj = image.getJSONObject(i);
                                String im = obj.getString("file_path");
                                more_image_array.add(im);

                            }

                            JSONArray image1 = jsonObject.getJSONArray("posters");
                            for (int i = 0; i < image1.length(); i++) {

                                JSONObject obj = image1.getJSONObject(i);
                                String im = obj.getString("file_path");
                                more_image_array.add(im);

                            }

                        } catch (Exception e) {
//                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request3);

        JsonObjectRequest request4 = new JsonObjectRequest(Request.Method.GET,
                urlPreId + imdb_id + similar_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray arrayMovies = jsonObject.getJSONArray("results");
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
                                urlThumbnail = currentmovie.getString("poster_path");


                                Movie movie = new Movie(id, title, releaseDate, audienceScore, synopsis, urlThumbnail);

                                if (id != -1 && !title.equals("NA")) {
                                    similar_list.add(movie);
                                }
                            }

                        } catch (Exception e) {
//                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request4);

        JsonObjectRequest request5 = new JsonObjectRequest(Request.Method.GET,
                urlPreId + imdb_id + reviews_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {


                            JSONArray rvw = jsonObject.getJSONArray("results");

                            for (int i = 0; i < rvw.length(); i++) {

                                JSONObject current = rvw.getJSONObject(i);
                                String author = current.getString("author");
                                String text = current.getString("content");

                                Movie movie = new Movie(author, text);
                                reviews.add(movie);

                            }


                        } catch (Exception e) {
                           // Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, e.toString(),
                             //       Toast.LENGTH_LONG)
                             //       .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request5);


        final String movie_trailer = "https://www.youtube.com/watch?v=" + trailer;

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer));
                    startActivity(intent);
                }catch (ActivityNotFoundException ex){
                    Intent intent=new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v="+trailer));
                    startActivity(intent);
                }


            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = getLayoutInflater().inflate(R.layout.cast_crew_recycler, null);

                Adapter_cast_and_crew castAdapter = new Adapter_cast_and_crew(IMDB_Movie_Details_Top_Bottom.this,
                        cast_and_crew);
                RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.cast_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(IMDB_Movie_Details_Top_Bottom.this));
                recyclerView.setAdapter(castAdapter);


                recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(IMDB_Movie_Details_Top_Bottom.this,
                        recyclerView, new ClickListener() {
                    @Override
                    public void onCLick(View v, int position) {
                        //  Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Touched on: " +
                        //       position, Toast.LENGTH_LONG).show();

                        Movie movie = cast_and_crew.get(position);
                        String id = String.valueOf(movie.getId());

                        Intent intent = new Intent(IMDB_Movie_Details_Top_Bottom.this, Person_Details.class);
                        intent.putExtra("tv", id);


                        startActivity(intent);


                    }

                    @Override
                    public void onLongClick(View v, int position) {

                        // Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Long Touched on: " +
                        //   position, Toast.LENGTH_LONG).show();

                    }
                }));


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        IMDB_Movie_Details_Top_Bottom.this);

                if (cast_and_crew == null || cast_and_crew.size() == 0) {
                    Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "No Cast and crew Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();
                }

            }
        });

        tvHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homepage != null && homepage != "" && homepage.length() != 0) {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(homepage));
                    startActivity(i);
                } else {
                    Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "No Link is Available", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        btn_moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = getLayoutInflater().inflate(R.layout.custom_more_image, null);
                ListView oddView = (ListView) view1.findViewById(R.id.odd_list_image);
                ListView evenView = (ListView) view1.findViewById(R.id.even_list_image);


                if (more_image_array != null && more_image_array.size() != 0) {

                    for (int i = 0; i < more_image_array.size(); i++) {

                        if (i % 2 == 0) {
                            evenArray.add(more_image_array.get(i));
                        } else {
                            oddArray.add(more_image_array.get(i));
                        }

                    }

                }

                Adapter_more_image oddAdapter = new Adapter_more_image(oddArray, IMDB_Movie_Details_Top_Bottom.this);
                Adapter_more_image evenAdapter = new Adapter_more_image(evenArray, IMDB_Movie_Details_Top_Bottom.this);

                oddView.setAdapter(oddAdapter);
                evenView.setAdapter(evenAdapter);

                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        IMDB_Movie_Details_Top_Bottom.this);

                if (more_image_array == null || more_image_array.size() == 0) {
                    Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "No image Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();

                }

            }
        });

        btn_similar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = getLayoutInflater().inflate(R.layout.cast_crew_recycler, null);

                AdapterBoxOffice castAdapter = new AdapterBoxOffice(IMDB_Movie_Details_Top_Bottom.this);
                castAdapter.setMovies(similar_list);
                RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.cast_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(IMDB_Movie_Details_Top_Bottom.this));
                recyclerView.setAdapter(castAdapter);

                recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(IMDB_Movie_Details_Top_Bottom.this,
                        recyclerView, new ClickListener() {
                    @Override
                    public void onCLick(View v, int position) {
                      //  Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Touched on: " +
                              //  position, Toast.LENGTH_LONG).show();

                        Movie movie = similar_list.get(position);
                        String id = String.valueOf(movie.getId());

                        Intent intent = new Intent(IMDB_Movie_Details_Top_Bottom.this, Movie_Details.class);
                        intent.putExtra("tv", id);


                        startActivity(intent);


                    }

                    @Override
                    public void onLongClick(View v, int position) {

                     //   Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Long Touched on: " +
                       //         position, Toast.LENGTH_LONG).show();

                    }
                }));


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        IMDB_Movie_Details_Top_Bottom.this);

                if (similar_list == null || similar_list.size() == 0) {
                    Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "No Similar Movie Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();
                }

            }
        });

        btn_reviws.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = getLayoutInflater().inflate(R.layout.review, null);
                ListView oddView = (ListView) view1.findViewById(R.id.review_list);

                Review_Adapter review_adapter = new Review_Adapter(reviews, IMDB_Movie_Details_Top_Bottom.this);

                oddView.setAdapter(review_adapter);

                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        IMDB_Movie_Details_Top_Bottom.this);

                if (reviews == null || reviews.size() == 0) {
                    Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "No Reviews Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();

                }

            }
        });


        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMovies = new DBMovies(IMDB_Movie_Details_Top_Bottom.this);
                boolean bool = dbMovies.checkWatch(w_id);
                final boolean boo2 = dbMovies.checkWish(w_id);
                if (bool) {

                    Snackbar.make(rootLayout, "Already in Watch List", Snackbar.LENGTH_LONG)
                            .setAction("Remove?", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dbMovies.deleteWatch(w_id);
                                    watch.setBackgroundResource(R.color.grey_60);
                                    if (!boo2) {
                                        layout1.setVisibility(View.VISIBLE);
                                        layout2.setVisibility(View.GONE);
                                        layout3.setVisibility(View.GONE);
                                    }
                                }
                            }).show();
                } else {
                    Movie movie = new Movie(w_id, w_name);
                    long fact = dbMovies.insertWatch(movie);
                    if (fact != -1) {
                        watch.setBackgroundResource(R.color.accent_color);
                        layout1.setVisibility(View.GONE);
                        Snackbar.make(rootLayout, "Added to Watch List", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });


        wish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMovies = new DBMovies(IMDB_Movie_Details_Top_Bottom.this);
                final boolean boo2 = dbMovies.checkWatch(w_id);
                boolean bool = dbMovies.checkWish(w_id);
                if (bool) {
                    Snackbar.make(rootLayout, "Alrea dy in Wish List", Snackbar.LENGTH_LONG)
                            .setAction("Remove?", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dbMovies.deleteWish(w_id);
                                    wish.setBackgroundResource(R.color.grey_60);
                                    if (!boo2) {
                                        layout1.setVisibility(View.VISIBLE);
                                        layout2.setVisibility(View.GONE);
                                        layout3.setVisibility(View.GONE);
                                    }
                                }
                            }).show();
                } else {
                    Movie movie = new Movie(w_id, w_name);
                    long fact = dbMovies.insertWish(movie);
                    if (fact != -1) {
                        wish.setBackgroundResource(R.color.primary_color_dark);
                        layout1.setVisibility(View.GONE);
                        Snackbar.make(rootLayout, "Added to Wish List", Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });


    }

    private void initualizing_contents() {

        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        coverLayout = (ImageView) findViewById(R.id.cover);
        coverLayout1 = (ImageView) findViewById(R.id.cover1);
        coverLayout1.setVisibility(View.GONE);
        circularImageView = (ImageView) findViewById(R.id.play_trailer);
        circularImageView1 = (ImageView) findViewById(R.id.play_trailer1);
        circularImageView1.setVisibility(View.GONE);
        //  ratingBar = (RatingBar) findViewById(R.id.movieAudienceScore_details);
        imageView = (ImageView) findViewById(R.id.postar_image_detail);
        button = (TextView) findViewById(R.id.cast_and_crew);
        // tvTitle = (TextView) findViewById(R.id.title_details);
        tvGenre = (TextView) findViewById(R.id.genre_details);
        tvOverview = (TextView) findViewById(R.id.overview_details);
        tvHomepage = (TextView) findViewById(R.id.homepage_details);
        tvProduction = (TextView) findViewById(R.id.production_details);
        tvGenreDown = (TextView) findViewById(R.id.genre_down_details);
        tvRevenue = (TextView) findViewById(R.id.revenue_details);
        tvTagLine = (TextView) findViewById(R.id.tagline_details);
        tvImbdId = (TextView) findViewById(R.id.imdb_details);
        tvRating = (TextView) findViewById(R.id.tv_audience);
        imageRating = (ImageView) findViewById(R.id.image_rating);
        Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(R.drawable.bookmark_toolbar).resize(70, 70)
                .into(imageRating);

        btn_moreImage = (TextView) findViewById(R.id.more_image);
        btn_reviws = (TextView) findViewById(R.id.review_details);
        btn_similar = (TextView) findViewById(R.id.similar_details);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie__details, menu);
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


}
