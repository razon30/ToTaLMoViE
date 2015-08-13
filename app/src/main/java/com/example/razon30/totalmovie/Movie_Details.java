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
import android.widget.ScrollView;
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


public class Movie_Details extends AppCompatActivity {

    public ArrayList<Movie> cast_and_crew = new ArrayList<Movie>();
    //similar
    public ArrayList<Movie> similar_list = new ArrayList<Movie>();
    //reviews
    public ArrayList<Movie> reviews = new ArrayList<Movie>();
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout rootLayout;
    ImageView coverLayout, coverLayout1;
    ImageView circularImageView, circularImageView1;
    ImageView imageView, imageRating;
    TextView button, btn_moreImage, btn_reviws, btn_similar;
    TextView tvGenre, tvOverview, tvHomepage, tvProduction, tvGenreDown, tvRevenue, tvTagLine,
            tvImbdId, tvRating;
    String urlPreId = "http://api.themoviedb.org/3/movie/";
    long id;
    String urlLaterId = "?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String image_url = "http://image.tmdb.org/t/p/w500";
    String vediopost = "/videos?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String cast_post = "/credits?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String image_post = "/images?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String similar_post = "/similar?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String reviews_post = "/reviews?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String trailer, homepage;
    String com_URL;
    //more image
    ListView oddList, evenList;
    ImageView more_image;
    ArrayList<String> oddArray = new ArrayList<String>();
    ArrayList<String> evenArray = new ArrayList<String>();
    ArrayList<String> more_image_array = new ArrayList<String>();
    ScrollView scrollView;
    ImageView image1, image2, image3;
    String iid1, iid2, iid3;
    ImageView similarImage1, similarImage2, similarImage3;
    String sid1, sid2, sid3;
    TextView similar_text1, similar_text2, similar_text3;
    ImageView castImage1, castImage2, castImage3;
    String cid1, cid2, cid3;
    TextView cast_text1, cast_text2, cast_text3;
    //watch and wish
    String w_id;
    String w_name;
    int a = 1;
    ImageView watch;
    ImageView wish, add;
    DBMovies dbMovies;
    LinearLayout layout1, layout2, layout3;
    private ImageLoader imageLoader;
    //Retriving data
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__details);
        final Intent intent = getIntent();
        id = Long.parseLong(intent.getStringExtra("tv"));
        w_id = String.valueOf(id);
        initualizing_contents();
        watch = (ImageView) findViewById(R.id.watch);
        wish = (ImageView) findViewById(R.id.wish);
        dbMovies = new DBMovies(Movie_Details.this);
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlPreId + id + urlLaterId, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            String backdrop_path = jsonObject.getString("backdrop_path");

                            if (backdrop_path != "" && backdrop_path != null) {

                                Picasso.with(Movie_Details.this).load(image_url + backdrop_path).into
                                        (coverLayout);

                                Picasso.with(Movie_Details.this).load(image_url + backdrop_path).into
                                        (coverLayout1);

                            } else {
                                Picasso.with(Movie_Details.this).load(R.drawable.ic_launcher).into
                                        (coverLayout);
                                //  coverLayout.setImageResource(R.drawable.ic_launcher);


                            }


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
                                tvOverview.setText("           " + overview);
                            } else {
                                tvOverview.setText("           " + "NA");
                            }


                            String poster_path = jsonObject.getString("poster_path");
                            if (poster_path != "" && poster_path != null) {

                                Picasso.with(Movie_Details.this).load(image_url + poster_path).into
                                        (imageView);

                            } else {
                                Picasso.with(Movie_Details.this).load(R.drawable.ic_launcher).into
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
                            // tvTitle.setText(title + "  (" + date[0] + ")");
                            w_name = title + "  (" + date[0] + ")";
                            //tvTitle.setText(w_name);
                            collapsingToolbarLayout.setTitle(title + "  (" + date[0] + ")");
                            // setTheme(R.style.MyCustomToolBarMovieDetails);
                            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style
                                    .ExpandedAppBar);
                            collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style
                                    .CollapsedAppBar);
                            collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style
                                    .ExpandedAppBarPlus1);
                            collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style
                                    .CollapsedAppBarPlus1);


                            String revenue = "";
                            revenue = jsonObject.getString("revenue");

                            if (revenue != null && revenue.length() != 0 && revenue != "" &&
                                    revenue != "0") {
                                tvRevenue.setText(revenue);
                            } else if (revenue == "0") {
                                tvRevenue.setText("Revenue Unknown");
                            } else {

                                tvRevenue.setText("Still Running, NO total Revenue");
                            }


                            String tagLine = "";

                            tagLine = jsonObject.getString("tagline");
                            if (tagLine != null && tagLine.length() != 0 && tagLine != "") {
                                tvTagLine.setVisibility(View.VISIBLE);
                                tvTagLine.setText("''" + tagLine + "''");
                            } else {
                                tvTagLine.setText("NA");
                                tvTagLine.setVisibility(View.GONE);
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
                urlPreId + id + vediopost, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
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
                urlPreId + id + cast_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray cast = jsonObject.getJSONArray("cast");

                            String name1 = cast.getJSONObject(0).getString("name");
                            String profile = cast.getJSONObject(0).getString("profile_path");
                            cid1 = cast.getJSONObject(0).getString("id");
                            if (name1 != null && name1.length() != 0) {
                                cast_text1.setVisibility(View.VISIBLE);
                                castImage1.setVisibility(View.VISIBLE);
                                cast_text1.setText(name1);
                                Picasso.with(Movie_Details.this).load(image_url + profile).into
                                        (castImage1);
                            } else {

                                cast_text1.setVisibility(View.GONE);
                                castImage1.setVisibility(View.GONE);
                            }


                            String name2 = cast.getJSONObject(1).getString("name");
                            String profile2 = cast.getJSONObject(1).getString("profile_path");
                            cid2 = cast.getJSONObject(1).getString("id");
                            if (name2 != null && name2.length() != 0) {
                                cast_text2.setVisibility(View.VISIBLE);
                                castImage2.setVisibility(View.VISIBLE);
                                cast_text2.setText(name2);
                                Picasso.with(Movie_Details.this).load(image_url + profile2).into
                                        (castImage2);
                            } else {

                                cast_text2.setVisibility(View.GONE);
                                castImage2.setVisibility(View.GONE);
                            }


                            String name3 = cast.getJSONObject(2).getString("name");
                            String profile3 = cast.getJSONObject(2).getString("profile_path");
                            cid3 = cast.getJSONObject(2).getString("id");
                            if (name3 != null && name3.length() != 0) {
                                cast_text3.setVisibility(View.VISIBLE);
                                castImage3.setVisibility(View.VISIBLE);
                                cast_text3.setText(name3);
                                Picasso.with(Movie_Details.this).load(image_url + profile3).into
                                        (castImage3);
                            } else {

                                cast_text3.setVisibility(View.GONE);
                                castImage3.setVisibility(View.GONE);
                            }


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
                urlPreId + id + image_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray image = jsonObject.getJSONArray("posters");

                            String profile1 = image.getJSONObject(1).getString("file_path");
                            if (profile1 != null && profile1.length() != 0) {
                                image1.setVisibility(View.VISIBLE);
                                Picasso.with(Movie_Details.this).load(image_url + profile1).into
                                        (image1);
                            } else {

                                image1.setVisibility(View.GONE);
                            }

                            String profile2 = image.getJSONObject(2).getString("file_path");
                            if (profile2 != null && profile2.length() != 0) {
                                image2.setVisibility(View.VISIBLE);
                                Picasso.with(Movie_Details.this).load(image_url + profile2).into
                                        (image2);
                            } else {

                                image2.setVisibility(View.GONE);
                            }


                            for (int i = 0; i < image.length(); i++) {

                                JSONObject obj = image.getJSONObject(i);
                                String im = obj.getString("file_path");
                                more_image_array.add(im);

                            }

                            JSONArray image11 = jsonObject.getJSONArray("posters");

                            String profile3 = image11.getJSONObject(0).getString("file_path");
                            if (profile3 != null && profile3.length() != 0) {
                                image3.setVisibility(View.VISIBLE);
                                Picasso.with(Movie_Details.this).load(image_url + profile3).into
                                        (image3);
                            } else {

                                image3.setVisibility(View.GONE);
                            }


                            for (int i = 0; i < image11.length(); i++) {

                                JSONObject obj = image11.getJSONObject(i);
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
                urlPreId + id + similar_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray arrayMovies = jsonObject.getJSONArray("results");


                            String name1 = arrayMovies.getJSONObject(0).getString("title");
                            String profile1 = arrayMovies.getJSONObject(0).getString
                                    ("poster_path");
                            sid1 = arrayMovies.getJSONObject(0).getString("id");
                            if (name1 != null && name1.length() != 0) {
                                similar_text1.setVisibility(View.VISIBLE);
                                similarImage1.setVisibility(View.VISIBLE);
                                similar_text1.setText(name1);
                                Picasso.with(Movie_Details.this).load(image_url + profile1).into
                                        (similarImage1);
                            } else {

                                similar_text1.setVisibility(View.GONE);
                                similarImage1.setVisibility(View.GONE);
                            }


                            String name2 = arrayMovies.getJSONObject(1).getString("title");
                            String profile2 = arrayMovies.getJSONObject(1).getString
                                    ("poster_path");
                            sid2 = arrayMovies.getJSONObject(1).getString("id");
                            if (name2 != null && name2.length() != 0) {
                                similar_text2.setVisibility(View.VISIBLE);
                                similarImage2.setVisibility(View.VISIBLE);
                                similar_text2.setText(name2);
                                Picasso.with(Movie_Details.this).load(image_url + profile2).into
                                        (similarImage2);
                            } else {

                                similar_text2.setVisibility(View.GONE);
                                similarImage2.setVisibility(View.GONE);
                            }


                            String name3 = arrayMovies.getJSONObject(2).getString("title");
                            String profile3 = arrayMovies.getJSONObject(2).getString
                                    ("poster_path");
                            sid3 = arrayMovies.getJSONObject(2).getString("id");
                            if (name3 != null && name3.length() != 0) {
                                similar_text3.setVisibility(View.VISIBLE);
                                similarImage3.setVisibility(View.VISIBLE);
                                similar_text3.setText(name3);
                                Picasso.with(Movie_Details.this).load(image_url + profile3).into
                                        (similarImage3);
                            } else {

                                similar_text3.setVisibility(View.GONE);
                                similarImage3.setVisibility(View.GONE);
                            }


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
                urlPreId + id + reviews_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
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
                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
                                    .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
                                .show();

                    }
                });

        requestQueue.add(request5);


        final String movie_trailer = "https://www.youtube.com/watch?v=" + trailer;

        circularImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer));
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + trailer));
                    startActivity(intent);
                }


            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = getLayoutInflater().inflate(R.layout.cast_crew_recycler, null);

                Adapter_cast_and_crew castAdapter = new Adapter_cast_and_crew(Movie_Details.this,
                        cast_and_crew);
                RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.cast_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(Movie_Details.this));
                recyclerView.setAdapter(castAdapter);


                recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(Movie_Details.this,
                        recyclerView, new ClickListener() {
                    @Override
                    public void onCLick(View v, int position) {
                        Toast.makeText(Movie_Details.this, "Touched on: " + position, Toast.LENGTH_LONG).show();

                        Movie movie = cast_and_crew.get(position);
                        String id = String.valueOf(movie.getId());

                        Intent intent = new Intent(Movie_Details.this, Person_Details.class);
                        intent.putExtra("tv", id);


                        startActivity(intent);


                    }

                    @Override
                    public void onLongClick(View v, int position) {

                        Toast.makeText(Movie_Details.this, "Long Touched on: " + position, Toast.LENGTH_LONG).show();

                    }
                }));


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        Movie_Details.this);

                if (cast_and_crew == null || cast_and_crew.size() == 0) {
                    Toast.makeText(Movie_Details.this, "No Cast and crew Found or Network Error",
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
                    Toast.makeText(Movie_Details.this, "No Link is Available", Toast.LENGTH_LONG)
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

                Adapter_more_image oddAdapter = new Adapter_more_image(oddArray, Movie_Details.this);
                Adapter_more_image evenAdapter = new Adapter_more_image(evenArray, Movie_Details.this);

                oddView.setAdapter(oddAdapter);
                evenView.setAdapter(evenAdapter);

                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        Movie_Details.this);

                if (more_image_array == null || more_image_array.size() == 0) {
                    Toast.makeText(Movie_Details.this, "No image Found or Network Error",
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

                AdapterBoxOffice castAdapter = new AdapterBoxOffice(Movie_Details.this);
                castAdapter.setMovies(similar_list);
                RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.cast_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(Movie_Details.this));
                recyclerView.setAdapter(castAdapter);

                recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(Movie_Details.this,
                        recyclerView, new ClickListener() {
                    @Override
                    public void onCLick(View v, int position) {
                        //  Toast.makeText(Movie_Details.this, "Touched on: " + position, Toast
                        //       .LENGTH_LONG).show();

                        Movie movie = similar_list.get(position);
                        String id = String.valueOf(movie.getId());

                        Intent intent = new Intent(Movie_Details.this, Movie_Details.class);
                        intent.putExtra("tv", id);


                        startActivity(intent);


                    }

                    @Override
                    public void onLongClick(View v, int position) {

                        // Toast.makeText(Movie_Details.this, "Long Touched on: " + position, Toast
                        //   .LENGTH_LONG).show();

                    }
                }));


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        Movie_Details.this);

                if (similar_list == null || similar_list.size() == 0) {
                    Toast.makeText(Movie_Details.this, "No Similar Movie Found or Network Error",
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

                Review_Adapter review_adapter = new Review_Adapter(reviews, Movie_Details.this);

                oddView.setAdapter(review_adapter);

                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        Movie_Details.this);

                if (reviews == null || reviews.size() == 0) {
                    Toast.makeText(Movie_Details.this, "No Reviews Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();

                }

            }
        });


        castImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Movie_Details.this, Person_Details.class);
                intent1.putExtra("tv", cid1);
                startActivity(intent1);
            }
        });

        castImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Movie_Details.this, Person_Details.class);
                intent1.putExtra("tv", cid2);
                startActivity(intent1);
            }
        });

        castImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Movie_Details.this, Person_Details.class);
                intent1.putExtra("tv", cid3);
                startActivity(intent1);
            }
        });

        similarImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Movie_Details.this, Movie_Details.class);
                intent1.putExtra("tv", sid1);
                startActivity(intent1);
            }
        });

        similarImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Movie_Details.this, Movie_Details.class);
                intent1.putExtra("tv", sid2);
                startActivity(intent1);
            }
        });

        similarImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Movie_Details.this, Movie_Details.class);
                intent1.putExtra("tv", sid3);
                startActivity(intent1);
            }
        });

//Toast.makeText(Movie_Details.this,w_name,Toast.LENGTH_LONG).show();

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMovies = new DBMovies(Movie_Details.this);
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
                dbMovies = new DBMovies(Movie_Details.this);
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

        // worksOnWatchWish(w_id,w_name);

    }

    private void worksOnWatchWish(final String w_id, final String w_name) {


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
        Picasso.with(Movie_Details.this).load(R.drawable.bookmark_toolbar).resize(70, 70)
                .into(imageRating);

        btn_moreImage = (TextView) findViewById(R.id.more_image);
        btn_reviws = (TextView) findViewById(R.id.review_details);
        btn_similar = (TextView) findViewById(R.id.similar_details);


        image1 = (ImageView) findViewById(R.id.movie_details_image1);
        image2 = (ImageView) findViewById(R.id.movie_details_image2);
        image3 = (ImageView) findViewById(R.id.movie_details_image3);

        similarImage1 = (ImageView) findViewById(R.id.movie_details_similar_image1);
        similarImage2 = (ImageView) findViewById(R.id.movie_details_similar_image2);
        similarImage3 = (ImageView) findViewById(R.id.movie_details_similar_image3);
        similar_text1 = (TextView) findViewById(R.id.movie_details_similar_name1);
        similar_text2 = (TextView) findViewById(R.id.movie_details_similar_name2);
        similar_text3 = (TextView) findViewById(R.id.movie_details_similar_name3);


        castImage1 = (ImageView) findViewById(R.id.movie_details_cast_image1);
        castImage2 = (ImageView) findViewById(R.id.movie_details_cast_image2);
        castImage3 = (ImageView) findViewById(R.id.movie_details_cast_image3);
        cast_text1 = (TextView) findViewById(R.id.movie_details_cast_name1);
        cast_text2 = (TextView) findViewById(R.id.movie_details_cast_name2);
        cast_text3 = (TextView) findViewById(R.id.movie_details_cast_name3);

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
    }


}
