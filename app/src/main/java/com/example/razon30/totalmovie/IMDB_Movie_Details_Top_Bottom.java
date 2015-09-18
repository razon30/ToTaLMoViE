package com.example.razon30.totalmovie;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import com.github.mrengineer13.snackbar.SnackBar;
import com.quinny898.library.persistentsearch.SearchBox;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;


public class IMDB_Movie_Details_Top_Bottom extends AppCompatActivity {

    public ArrayList<Movie> cast_and_crew = new ArrayList<Movie>();
    //similar
    public ArrayList<Movie> similar_list = new ArrayList<Movie>();
    //reviews
    public ArrayList<Movie> reviews = new ArrayList<Movie>();


    // CoordinatorLayout rootLayout;
    String w_id;
    String w_name;
    int a = 1;
    DBMovies dbMovies;
    LinearLayout layout1, layout2, layout3;
    ImageView coverLayout, coverLayout1;
    ImageView circularImageView, circularImageView1;
    ImageView imageView, imageRating;
    TextView button, btn_moreImage, btn_reviws, btn_similar;
    TextView tvOverview, tvHomepage, tvProduction, tvGenreDown, tvRevenue, tvTagLine,
            tvImbdId, tvRating, tvBudget, tvRuntime, tvVotenumber;
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
    String urlPre = "http://api.themoviedb.org/3/search/";
    String multiPost = "multi?api_key=f246d5e5105e9934d3cd4c4c181d618d&query=";
    String trailer, homepage;
    long current_id;
    ArrayList<String> movie_id = new ArrayList<String>();


    ImageView image1, image2, image3;
    String iid1, iid2, iid3;
    ImageView similarImage1, similarImage2, similarImage3;
    String sid1, sid2, sid3;
    TextView similar_text1, similar_text2, similar_text3;
    ImageView castImage1, castImage2, castImage3;
    String cid1, cid2, cid3;
    TextView cast_text1, cast_text2, cast_text3;
    //more image
    ListView oddList, evenList;
    ImageView more_image;
    ArrayList<String> oddArray = new ArrayList<String>();
    ArrayList<String> evenArray = new ArrayList<String>();
    ArrayList<String> more_image_array = new ArrayList<String>();
    CardView rating_card;
    ImageView watch;
    ImageView wish, add;
    TextView tvGenre;
    private ImageLoader imageLoader;
    //Retriving data
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private SearchBox search;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imdb__movie__details__top__bottom);
        Intent intent = getIntent();
        imdb_id = intent.getStringExtra("tv");
        w_id = String.valueOf(id);
        initualizing_contents();
        worksOncolor();
        worksOnNetwork();
        worksOnSearch(w_id);


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


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.VISIBLE);
            }
        });


        //  scrollView = (ScrollView) findViewById(R.id.movie_details);
        // toolbar = (Toolbar) findViewById(R.id.toolbar);

        //rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);



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


                            String vote = "";
                            vote = jsonObject.getString("vote_count");
                            if (vote != null) {
                                tvVotenumber.setText(vote);
                            } else {
                                tvVotenumber.setText("");
                            }

                            String runtime = "";
                            runtime = jsonObject.getString("runtime");
                            if (runtime != null) {
                                tvRuntime.setText(runtime + " minute");
                            } else {
                                tvRuntime.setText("Sorry,unknown");
                            }
                            String budget = "";
                            budget = jsonObject.getString("budget");

                            if (budget != null && budget.length() != 0 && budget != "" &&
                                    budget != "0") {
                                tvBudget.setText("$" + budget);
                            } else {
                                tvBudget.setText("Budget Unknown");
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

                            if (Build.VERSION.SDK_INT >= 22) {
                                CollapsingToolbarLayout collapsingToolbarLayout;
                                collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
                                collapsingToolbarLayout.setTitle(title + "  (" + date[0] + ")");
                                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style
                                        .ExpandedAppBar);
                                collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style
                                        .CollapsedAppBar);
                                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style
                                        .ExpandedAppBarPlus1);
                                collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style
                                        .CollapsedAppBarPlus1);
                            }

                            String revenue = "";
                            revenue = jsonObject.getString("revenue");

                            if (revenue != null && revenue.length() != 0 && revenue != "" && revenue
                                    != "0") {
                                tvRevenue.setText("$" + revenue);
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

                            String name1 = cast.getJSONObject(0).getString("name");
                            String profile = cast.getJSONObject(0).getString("profile_path");
                            cid1 = cast.getJSONObject(0).getString("id");
                            if (name1 != null && name1.length() != 0) {
                                cast_text1.setVisibility(View.VISIBLE);
                                castImage1.setVisibility(View.VISIBLE);
                                cast_text1.setText(name1);
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + profile).into
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
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + profile2).into
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
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + profile3).into
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

                            JSONArray image11 = jsonObject.getJSONArray("posters");
                            for (int i = 0; i < image11.length(); i++) {

                                JSONObject obj = image11.getJSONObject(i);
                                String im = obj.getString("file_path");
                                more_image_array.add(im);

                            }
                            String profile1 = image11.getJSONObject(1).getString("file_path");
                            if (profile1 != null && profile1.length() != 0) {
                                image1.setVisibility(View.VISIBLE);
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + profile1).into
                                        (image1);
                            } else {

                                image1.setVisibility(View.GONE);
                            }

                            String profile2 = image11.getJSONObject(2).getString("file_path");
                            if (profile2 != null && profile2.length() != 0) {
                                image2.setVisibility(View.VISIBLE);
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + profile2).into
                                        (image2);
                            } else {

                                image2.setVisibility(View.GONE);
                            }

                            String profile3 = image11.getJSONObject(0).getString("file_path");
                            if (profile3 != null && profile3.length() != 0) {
                                image3.setVisibility(View.VISIBLE);
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + profile3).into
                                        (image3);
                            } else {

                                image3.setVisibility(View.GONE);
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
                            String name1 = arrayMovies.getJSONObject(0).getString("title");
                            String profile1 = arrayMovies.getJSONObject(0).getString
                                    ("poster_path");
                            sid1 = arrayMovies.getJSONObject(0).getString("id");
                            if (name1 != null && name1.length() != 0) {
                                similar_text1.setVisibility(View.VISIBLE);
                                similarImage1.setVisibility(View.VISIBLE);
                                similar_text1.setText(name1);
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + profile1).into
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
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + profile2).into
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
                                Picasso.with(IMDB_Movie_Details_Top_Bottom.this).load(image_url + profile3).into
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

        castImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IMDB_Movie_Details_Top_Bottom.this, Person_Details.class);
                intent1.putExtra("tv", cid1);
                startActivity(intent1);
            }
        });

        castImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IMDB_Movie_Details_Top_Bottom.this, Person_Details.class);
                intent1.putExtra("tv", cid2);
                startActivity(intent1);
            }
        });

        castImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IMDB_Movie_Details_Top_Bottom.this, Person_Details.class);
                intent1.putExtra("tv", cid3);
                startActivity(intent1);
            }
        });

        similarImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IMDB_Movie_Details_Top_Bottom.this, Movie_Details.class);
                intent1.putExtra("tv", sid1);
                startActivity(intent1);
            }
        });

        similarImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IMDB_Movie_Details_Top_Bottom.this, Movie_Details.class);
                intent1.putExtra("tv", sid2);
                startActivity(intent1);
            }
        });

        similarImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(IMDB_Movie_Details_Top_Bottom.this, Movie_Details.class);
                intent1.putExtra("tv", sid3);
                startActivity(intent1);
            }
        });


        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMovies = new DBMovies(IMDB_Movie_Details_Top_Bottom.this);
                boolean bool = dbMovies.checkWatch(w_id);
                final boolean boo2 = dbMovies.checkWish(w_id);
                if (bool) {
                    new SnackBar.Builder(IMDB_Movie_Details_Top_Bottom.this)
                            .withMessage("Already in Watch List") // OR
                            .withActionMessage("Remove") // OR
                            .withTextColorId(R.color.accent_color)
                            .withBackgroundColorId(R.color.primaryColor)
                            .withOnClickListener(new SnackBar.OnMessageClickListener() {
                                @Override
                                public void onMessageClick(Parcelable parcelable) {
                                    dbMovies.deleteWatch(w_id);
                                    watch.setBackgroundResource(R.color.grey_60);
                                }
                            })
                            .show();



                } else {
                    Movie movie = new Movie(w_id, w_name);
                    long fact = dbMovies.insertWatch(movie);
                    if (fact != -1) {
                        watch.setBackgroundResource(R.color.accent_color);

                        new SnackBar.Builder(IMDB_Movie_Details_Top_Bottom.this)
                                .withMessage("Added to Watch List") // OR
                                .withTextColorId(R.color.accent_color)
                                .withBackgroundColorId(R.color.primaryColor)
                                .show();


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

                    new SnackBar.Builder(IMDB_Movie_Details_Top_Bottom.this)
                            .withMessage("Already in Wish List") // OR
                            .withActionMessage("Remove") // OR
                            .withTextColorId(R.color.accent_color)
                            .withBackgroundColorId(R.color.primaryColor)
                            .withOnClickListener(new SnackBar.OnMessageClickListener() {
                                @Override
                                public void onMessageClick(Parcelable parcelable) {
                                    dbMovies.deleteWish(w_id);
                                    wish.setBackgroundResource(R.color.grey_60);
                                }
                            })
                            .show();


                } else {
                    Movie movie = new Movie(w_id, w_name);
                    long fact = dbMovies.insertWish(movie);
                    if (fact != -1) {
                        wish.setBackgroundResource(R.color.accent_color);
                        new SnackBar.Builder(IMDB_Movie_Details_Top_Bottom.this)
                                .withMessage("Added to Wish List") // OR
                                .withTextColorId(R.color.accent_color)
                                .withBackgroundColorId(R.color.primaryColor)
                                .show();

                    }
                }
            }
        });


    }

    private void worksOnSearch(final String w_id) {
        //for search
        search = (SearchBox) findViewById(R.id.searchbox);
        // search.enableVoiceRecognition(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                dbMovies = new DBMovies(IMDB_Movie_Details_Top_Bottom.this);

                if (item.getItemId() == R.id.action_search) {
                    openSearch();
                }
                if (item.getItemId() == R.id.refresh) {
                    Intent intent = new Intent(IMDB_Movie_Details_Top_Bottom.this, IMDB_Movie_Details_Top_Bottom.class);
                    intent.putExtra("tv", w_id);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.clearWatch) {

                    dbMovies.deleteAllWatch();
                    Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Watch List cleared", Toast.LENGTH_LONG).show();
                }
                if (item.getItemId() == R.id.clearWish) {
                    dbMovies.deleteAllWish();
                    Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Wish List cleared", Toast.LENGTH_LONG).show();
                }
                if (item.getItemId() == R.id.about) {
                    Intent intent = new Intent(IMDB_Movie_Details_Top_Bottom.this, Credit.class);
                    startActivity(intent);
                }

                return true;
            }
        });
    }

    private void worksOnNetwork() {
        //progressDialouge();

        if (!isNetworkAvailable()) {

            AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                    IMDB_Movie_Details_Top_Bottom.this);

            builderAlertDialog.setTitle("Connection Failed")
                    .setMessage("Try for connecting?")
                    .setIcon(R.drawable.ic_action_warning)
                    .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

                        }
                    })
                    .setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        }

    }

    private void initualizing_contents() {

        rating_card = (CardView) findViewById(R.id.rating_card);
        // rating_card.setBackgroundColor(getResources().getColor(R.color.background2));
        //rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        coverLayout = (ImageView) findViewById(R.id.cover);
//        coverLayout1 = (ImageView) findViewById(R.id.cover1);
//        coverLayout1.setVisibility(View.GONE);
        circularImageView = (ImageView) findViewById(R.id.play_trailer);
//        circularImageView1 = (ImageView) findViewById(R.id.play_trailer1);
//        circularImageView1.setVisibility(View.GONE);
        //  ratingBar = (RatingBar) findViewById(R.id.movieAudienceScore_details);
        imageView = (ImageView) findViewById(R.id.postar_image_detail);
        button = (TextView) findViewById(R.id.cast_and_crew);
        // tvTitle = (TextView) findViewById(R.id.title_details);
        tvGenre = (TextView) findViewById(R.id.genre_details);
        tvOverview = (TextView) findViewById(R.id.overview_details);
        tvRuntime = (TextView) findViewById(R.id.runtime_details);
        tvHomepage = (TextView) findViewById(R.id.homepage_details);
        tvProduction = (TextView) findViewById(R.id.production_details);
        tvGenreDown = (TextView) findViewById(R.id.genre_down_details);
        tvRevenue = (TextView) findViewById(R.id.revenue_details);
        tvBudget = (TextView) findViewById(R.id.budget_details);
        tvTagLine = (TextView) findViewById(R.id.tagline_details);
        tvImbdId = (TextView) findViewById(R.id.imdb_details);
        tvRating = (TextView) findViewById(R.id.tv_audience);
        imageRating = (ImageView) findViewById(R.id.image_rating);
//        Picasso.with(Movie_Details.this).load(R.drawable.bookmark_toolbar).resize(70, 70)
//                .into(imageRating);

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
        watch = (ImageView) findViewById(R.id.watch);
        wish = (ImageView) findViewById(R.id.wish);
        add = (ImageView) findViewById(R.id.multiple_actions);
        tvVotenumber = (TextView) findViewById(R.id.vote_number);
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

    public void openSearch() {
        toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);
//        for (int x = 0; x < 10; x++) {
//            SearchResult option = new SearchResult("Result "
//                    + Integer.toString(x), getResources().getDrawable(
//                    R.drawable.ic_history));
//            //  search.addSearchable(option);
//        }
//        search.setMenuListener(new SearchBox.MenuListener() {
//
//            @Override
//            public void onMenuClick() {
//                // Hamburger has been clicked
//                Toast.makeText(MainActivity.this, "Menu click",
//                        Toast.LENGTH_LONG).show();
//            }
//
//        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
            }

            @Override
            public void onSearchTermChanged() {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
//                Toast.makeText(MainActivity.this, searchTerm + " Searched",
//                        Toast.LENGTH_LONG).show();
                // toolbar.setTitle(searchTerm);

                if (searchTerm != null && searchTerm.length() != 0 && searchTerm != "") {

                    String key;
                    String[] search = searchTerm.split(" ");
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < search.length; i++) {
                        builder.append(search[i]);
                        if (i < search.length - 1) {
                            builder.append("+");
                        }
                    }

                    //searchTerm = searchTerm.replaceAll("\\s", "");

                    key = urlPre + multiPost + builder;

                    Intent intent = new Intent(IMDB_Movie_Details_Top_Bottom.this, Multi_Search_Activity.class);
                    intent.putExtra("tv", key);
                    startActivity(intent);

                } else {
                    Toast.makeText(IMDB_Movie_Details_Top_Bottom.this, "Not Proper Keyword", Toast.LENGTH_LONG).show();
                    return;
                }

            }

            @Override
            public void onSearchCleared() {

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        if (search.getSearchText().isEmpty()) toolbar.setTitle("");
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void worksOncolor() {

        Random random = new Random();
        int i = random.nextInt(11 - 1 + 1) + 1;

        if (i == 1) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_one_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_one_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_one_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_one_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_one_toolbar));


            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.Style_one_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color.Style_one_navigationBar));
            }
        }
        if (i == 2) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_three_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_three_navigationBar));
            }
        }
        if (i == 3) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_three_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_three_navigationBar));
            }
        }
        if (i == 4) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_four_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_four_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_four_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_four_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_four_toolbar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_four_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_four_navigationBar));
            }
        }
        if (i == 5) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_five_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_five_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_five_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_five_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_five_toolbar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_five_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_five_navigationBar));
            }
        }
        if (i == 6) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_six_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_six_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_six_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_six_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_six_toolbar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_six_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_six_navigationBar));
            }
        }
        if (i == 7) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_seven_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_seven_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_seven_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_seven_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_seven_toolbar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_seven_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_seven_navigationBar));
            }
        }
        if (i == 8) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_eight_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_eight_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_eight_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_eight_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_eight_toolbar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_eight_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_eight_navigationBar));
            }
        }
        if (i == 9) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_nine_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_nine_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_nine_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_nine_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_nine_toolbar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_nine_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_nine_navigationBar));
            }
        }
        if (i == 10) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_ten_toolbar));
            add.setBackgroundColor(getResources().getColor(R.color.Style_ten_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_ten_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_ten_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_ten_toolbar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_ten_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_ten_navigationBar));
            }
        }
        if (i == 11) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_eleven_view));
            add.setBackgroundColor(getResources().getColor(R.color.Style_eleven_view));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_eleven_view));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_eleven_view));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_eleven_view));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.Style_eleven_view));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_eleven_view));
            }
        }
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
