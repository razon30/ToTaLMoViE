package com.example.razon30.totalmovie;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ScrollView;
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
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnPublishListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import dmax.dialog.SpotsDialog;

//import net.steamcrafted.loadtoast.LoadToast;
//import cn.pedant.SweetAlert.SweetAlertDialog;


public class Movie_Details extends AppCompatActivity {

    public ArrayList<Movie> cast_and_crew = new ArrayList<Movie>();
    //similar
    public ArrayList<Movie> similar_list = new ArrayList<Movie>();
    //reviews
    public ArrayList<Movie> reviews = new ArrayList<Movie>();
    // CollapsingToolbarLayout collapsingToolbarLayout;
    // CoordinatorLayout rootLayout;
    ImageView coverLayout, coverLayout1;
    ImageView circularImageView, circularImageView1;
    ImageView imageView, imageRating;
    TextView button, btn_moreImage, btn_reviws, btn_similar;
    TextView tvOverview, tvHomepage, tvProduction, tvGenreDown, tvRevenue, tvTagLine,
            tvImbdId, tvRating, tvBudget, tvRuntime, tvVotenumber, tvAwards;
    String urlPreId = "http://api.themoviedb.org/3/movie/";
    long id;
    String urlLaterId = "?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String image_url = "http://image.tmdb.org/t/p/w500";
    String vediopost = "/videos?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String cast_post = "/credits?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String image_post = "/images?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String similar_post = "/similar?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String reviews_post = "/reviews?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String urlPre = "http://api.themoviedb.org/3/search/";
    String multiPost = "multi?api_key=f246d5e5105e9934d3cd4c4c181d618d&query=";
    String imdb_details_url_pre = "http://www.omdbapi.com/?i=";
    String imdb_details_url_id;
    String imdb_details_url_post = "&plot=full&r=json";
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
    DBMovies dbMovies;
    LinearLayout layout1, layout2, layout3;
    CardView rating_card;
    ImageView watch;
    ImageView wish, add;
    TextView tvGenre;
    //for share on facebook
    String movie_name = "", description = "", image_link = "", movie_link = "";
    SimpleFacebook mSimpleFacebook;
    Permission[] permissions = new Permission[]{
            Permission.USER_PHOTOS,
            Permission.EMAIL,
            Permission.PUBLISH_ACTION
    };
    private ImageLoader imageLoader;
    //Retriving data
    private VolleySingleton volleySingleton;

    //for Slide
//    SlideShowView slideShowView;
//    SlideShowAdapter adapter;
    private RequestQueue requestQueue;
    private SearchBox search;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie__details);
        SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
                .setAppId("625994234086470")
                .setNamespace("sromkuapp")
                .setPermissions(permissions)
                .build();
        SimpleFacebook.setConfiguration(configuration);
        mSimpleFacebook = SimpleFacebook.getInstance();

        final Intent intent = getIntent();
        id = Long.parseLong(intent.getStringExtra("tv"));
        w_id = String.valueOf(id);
        initualizing_contents();
        worksOncolor();
        worksOnNetwork();
        worksOnSearch(w_id);


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

            wish.setBackgroundResource(R.color.accent_color);

        }

//        if (bool1 | bool2) {
//            layout1.setVisibility(View.GONE);
//            layout2.setVisibility(View.VISIBLE);
//            layout3.setVisibility(View.VISIBLE);
//        } else {
//            layout1.setVisibility(View.VISIBLE);
//            layout2.setVisibility(View.GONE);
//            layout3.setVisibility(View.GONE);
//        }

//        final OnPublishListener onPublishListener = new OnPublishListener() {
//            @Override
//            public void onComplete(String postId) {
//                //Log.i(TAG, "Published successfully. The new post id = " + postId);
//            }
//
//        };

//        final Feed feed = new Feed.Builder()
//                .setMessage("Test App 1...")
//                .setName("Simple Facebook SDK for Android")
//                .setCaption("Code less, do the same.")
//                .setDescription("Login, publish feeds and stories, invite friends and more...")
//                .setPicture("https://raw.github.com/sromku/android-simple-facebook/master/Refs/android_facebook_sdk_logo.png")
//                .setLink("https://github.com/razon30")
//                .addAction("Clone", "https://github.com/razon30")
//                .addProperty("Full documentation", "https://github.com/razon30")
//                .addProperty("Stars", "14")
//                .build();
//
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                layout2.setVisibility(View.VISIBLE);
////                layout3.setVisibility(View.VISIBLE);
//
//                mSimpleFacebook.publish(feed, true, onPublishListener);
//
//
//
//            }
//        });


        //  scrollView = (ScrollView) findViewById(R.id.movie_details);
//        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
//        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
//

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();


        MyAsyncTask task = new MyAsyncTask(Movie_Details.this);
        task.execute();


//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlPreId + id + urlLaterId, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        if (jsonObject == null || jsonObject.length() == 0) {
//                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//
//                        try {
//
//                            String backdrop_path = jsonObject.getString("backdrop_path");
//
//                            if (backdrop_path != "" && backdrop_path != null) {
//
//                                Picasso.with(Movie_Details.this).load(image_url + backdrop_path).into
//                                        (coverLayout);
//
////                                Picasso.with(Movie_Details.this).load(image_url + backdrop_path).into
////                                        (coverLayout1);
//
//                            } else {
//                                Picasso.with(Movie_Details.this).load(R.drawable.ic_launcher).into
//                                        (coverLayout);
//                                  coverLayout.setImageResource(R.drawable.ic_launcher);
//
//
//                            }
//
//                            image_link=image_url+backdrop_path;
//
//
//                            JSONArray genr = jsonObject.getJSONArray("genres");
//                            StringBuilder sb = new StringBuilder();
//                            for (int i = 0; i < genr.length(); i++) {
//                                JSONObject g = genr.getJSONObject(i);
//                                sb.append(g.getString("name"));
//                                if (i < genr.length() - 1) {
//                                    sb.append(",");
//                                }
//                            }
//                            tvGenre.setText(sb);
//                            tvGenreDown.setText(sb);
//
//                            description = String.valueOf(sb);
//
//                            homepage = "";
//                            homepage = jsonObject.getString("homepage");
//                            if (homepage != "" && homepage != null) {
//                                tvHomepage.setText("Home Page:  " + homepage);
//                            } else {
//                                tvHomepage.setText("Home Page:  " + "NA");
//                            }
//
//                            movie_link = homepage;
//
//                            String imdb_id = "";
//                            imdb_details_url_id = jsonObject.getString("imdb_id");
//                            if (imdb_details_url_id != "" && imdb_details_url_id != null) {
//                                tvImbdId.setText(imdb_details_url_id);
//
//                            } else {
//                                tvImbdId.setText("NA");
//                            }
//
//                            worksOnRating(imdb_details_url_id);
//
//
//
//
//
////                            String vote = "";
////                            vote = jsonObject.getString("vote_count");
////                            if (vote != null) {
////                                tvVotenumber.setText(vote);
////                            } else {
////                                tvVotenumber.setText("");
////                            }
////                            double audience_score = -1;
////
////                            audience_score = jsonObject.getDouble("vote_average");
////
////                            if (audience_score == -1) {
////                                tvRating.setText(audience_score + "");
////
////                            } else {
////                                tvRating.setText(audience_score + "");
////
////                            }
//
//                            String runtime = "";
//                            runtime = jsonObject.getString("runtime");
//                            if (runtime != null) {
//                                tvRuntime.setText(runtime + " minute");
//                            } else {
//                                tvRuntime.setText("Sorry,unknown");
//                            }
//                            String budget = "";
//                            budget = jsonObject.getString("budget");
//
//                            if (budget != null && budget.length() != 0 && budget != "" &&
//                                    budget != "0") {
//                                tvBudget.setText("$" + budget);
//                            } else {
//                                tvBudget.setText("Budget Unknown");
//                            }
//
//                            String overview = "";
//                            overview = jsonObject.getString("overview");
//                            if (overview != "" && overview != null) {
//                                tvOverview.setText("           " + overview);
//                            } else {
//                                tvOverview.setText("           " + "NA");
//                            }
//
//
//                            String poster_path = jsonObject.getString("poster_path");
//                            if (poster_path != "" && poster_path != null) {
//
//                                Picasso.with(Movie_Details.this).load(image_url + poster_path).into
//                                        (imageView);
//
//                            } else {
//                                Picasso.with(Movie_Details.this).load(R.drawable.ic_launcher).into
//                                        (imageView);
//                                imageView.setImageResource(R.drawable.ic_launcher);
//                            }
//                            JSONArray produc = jsonObject.getJSONArray("production_companies");
//                            StringBuilder production = new StringBuilder();
//                            for (int i = 0; i < produc.length(); i++) {
//
//                                JSONObject p = produc.getJSONObject(i);
//                                production.append(p.getString("name"));
//                                if (i < produc.length() - 1) {
//                                    production.append("\n");
//                                }
//
//                            }
//                            tvProduction.setText(production);
//
//                            String release_Date = jsonObject.getString("release_date");
//                            String[] date = release_Date.split("-");
//                            String title = jsonObject.getString("title");
//                            // tvTitle.setText(title + "  (" + date[0] + ")");
//                            w_name = title + "  (" + date[0] + ")";
//                            //tvTitle.setText(w_name);
//                            movie_name = title + "  (" + date[0] + ")";
//                            if(Build.VERSION.SDK_INT>=22) {
//                                CollapsingToolbarLayout collapsingToolbarLayout;
//                                collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
//                                collapsingToolbarLayout.setTitle(title + "  (" + date[0] + ")");
//                                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style
//                                        .ExpandedAppBar);
//                                collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style
//                                        .CollapsedAppBar);
//                                collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style
//                                        .ExpandedAppBarPlus1);
//                                collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style
//                                        .CollapsedAppBarPlus1);
//                            }
//
//
//                            String revenue = "";
//                            revenue = jsonObject.getString("revenue");
//
//                            if (revenue != null && revenue.length() != 0 && revenue != "" &&
//                                    revenue != "0") {
//                                tvRevenue.setText("$" + revenue);
//                            } else if (revenue == "0") {
//                                tvRevenue.setText("Revenue Unknown");
//                            } else {
//
//                                tvRevenue.setText("Still Running, NO total Revenue");
//                            }
//
//                            String tagLine = "";
//
//                            tagLine = jsonObject.getString("tagline");
//                            if (tagLine != null && tagLine.length() != 0 && tagLine != "") {
//                                tvTagLine.setVisibility(View.VISIBLE);
//                                tvTagLine.setText("''" + tagLine + "''");
//                            } else {
//                                tvTagLine.setText("NA");
//                                tvTagLine.setVisibility(View.GONE);
//                            }
//
//
//                        } catch (Exception e) {
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
//
//                    }
//                });
//
//        requestQueue.add(request);
//
////        JsonObjectRequest request_imdb = new JsonObjectRequest(Request
////                .Method.GET, imdb_details_url_pre + imdb_details_url_id + imdb_details_url_post, null,
////
////                new Response.Listener<JSONObject>() {
////                    @Override
////                    public void onResponse(JSONObject jsonObject) {
////
////                        if (jsonObject == null || jsonObject.length() == 0) {
////                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
////                                    .show();
////
////                        }
////
////                        try {
////
////                            String vote = "";
////                            vote = jsonObject.getString("imdbVotes");
////                            if (vote != null) {
////                                tvVotenumber.setText(vote);
////                            } else {
////                                tvVotenumber.setText("");
////                            }
////                            double audience_score = -1;
////
////                            audience_score = jsonObject.getDouble("imdbRating");
////
////                            if (audience_score == -1) {
////                                tvRating.setText(audience_score + "");
////
////                            } else {
////                                tvRating.setText(audience_score + "");
////
////                            }
////
////
////
////                        } catch (Exception e) {
////
////
////                        }
////
////
////                    }
////                },
////                new Response.ErrorListener() {
////                    @Override
////                    public void onErrorResponse(VolleyError volleyError) {
////
////                    }
////                });
////
////        requestQueue.add(request_imdb);
//
//
//        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET,
//                urlPreId + id + vediopost, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        if (jsonObject == null || jsonObject.length() == 0) {
//                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//
//                        try {
//
//                            JSONArray ved = jsonObject.getJSONArray("results");
//
//                            JSONObject v = ved.getJSONObject(0);
//                            trailer = v.getString("key");
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
//        requestQueue.add(request1);
//
//        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET,
//                urlPreId + id + cast_post, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        if (jsonObject == null || jsonObject.length() == 0) {
//                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//
//                        try {
//
//                            JSONArray cast = jsonObject.getJSONArray("cast");
//
//                            String name1 = cast.getJSONObject(0).getString("name");
//                            String profile = cast.getJSONObject(0).getString("profile_path");
//                            cid1 = cast.getJSONObject(0).getString("id");
//                            if (name1 != null && name1.length() != 0) {
//                                cast_text1.setVisibility(View.VISIBLE);
//                                castImage1.setVisibility(View.VISIBLE);
//                                cast_text1.setText(name1);
//                                Picasso.with(Movie_Details.this).load(image_url + profile).into
//                                        (castImage1);
//                            } else {
//
//                                cast_text1.setVisibility(View.GONE);
//                                castImage1.setVisibility(View.GONE);
//                            }
//
//
//                            String name2 = cast.getJSONObject(1).getString("name");
//                            String profile2 = cast.getJSONObject(1).getString("profile_path");
//                            cid2 = cast.getJSONObject(1).getString("id");
//                            if (name2 != null && name2.length() != 0) {
//                                cast_text2.setVisibility(View.VISIBLE);
//                                castImage2.setVisibility(View.VISIBLE);
//                                cast_text2.setText(name2);
//                                Picasso.with(Movie_Details.this).load(image_url + profile2).into
//                                        (castImage2);
//                            } else {
//
//                                cast_text2.setVisibility(View.GONE);
//                                castImage2.setVisibility(View.GONE);
//                            }
//
//
//                            String name3 = cast.getJSONObject(2).getString("name");
//                            String profile3 = cast.getJSONObject(2).getString("profile_path");
//                            cid3 = cast.getJSONObject(2).getString("id");
//                            if (name3 != null && name3.length() != 0) {
//                                cast_text3.setVisibility(View.VISIBLE);
//                                castImage3.setVisibility(View.VISIBLE);
//                                cast_text3.setText(name3);
//                                Picasso.with(Movie_Details.this).load(image_url + profile3).into
//                                        (castImage3);
//                            } else {
//
//                                cast_text3.setVisibility(View.GONE);
//                                castImage3.setVisibility(View.GONE);
//                            }
//
//
//                            for (int i = 0; i < cast.length(); i++) {
//
//                                JSONObject current_cast = cast.getJSONObject(i);
//                                String job = current_cast.getString("character");
//                                String name = current_cast.getString("name");
//                                String profile_thumbnail = current_cast.getString("profile_path");
//                                long id = current_cast.getLong("id");
//
//                                Movie movie = new Movie(name, job, profile_thumbnail, id);
//                                cast_and_crew.add(movie);
//
//                            }
//
//                            JSONArray crew = jsonObject.getJSONArray("crew");
//                            for (int i = 0; i < crew.length(); i++) {
//
//                                JSONObject current_cast = crew.getJSONObject(i);
//                                String job = current_cast.getString("job");
//                                String name = current_cast.getString("name");
//                                String profile_thumbnail = current_cast.getString("profile_path");
//                                long id = current_cast.getLong("id");
//
//                                Movie movie = new Movie(name, job, profile_thumbnail, id);
//                                cast_and_crew.add(movie);
//
//                            }
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
//        requestQueue.add(request2);
//
//        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET,
//                urlPreId + id + image_post, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        if (jsonObject == null || jsonObject.length() == 0) {
//                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//
//                        try {
//
//
//                            JSONArray image_slide = jsonObject.getJSONArray("backdrops");
//
////                            ArrayList<String> arrayList = new ArrayList<String>();
////
////                            for (int i = 0; i < image_.length(); i++) {
////
////                                JSONObject obj = image11.getJSONObject(i);
////                                String im = obj.getString("file_path");
////                                more_image_array.add(im);
////
////                            }
//
//
//                            JSONArray image = jsonObject.getJSONArray("posters");
//
//                            String profile1 = image.getJSONObject(1).getString("file_path");
//                            if (profile1 != null && profile1.length() != 0) {
//                                image1.setVisibility(View.VISIBLE);
//                                Picasso.with(Movie_Details.this).load(image_url + profile1).into
//                                        (image1);
//                            } else {
//
//                                image1.setVisibility(View.GONE);
//                            }
//
//                            String profile2 = image.getJSONObject(2).getString("file_path");
//                            if (profile2 != null && profile2.length() != 0) {
//                                image2.setVisibility(View.VISIBLE);
//                                Picasso.with(Movie_Details.this).load(image_url + profile2).into
//                                        (image2);
//                            } else {
//
//                                image2.setVisibility(View.GONE);
//                            }
//
//                            String profile3 = image.getJSONObject(0).getString("file_path");
//                            if (profile3 != null && profile3.length() != 0) {
//                                image3.setVisibility(View.VISIBLE);
//                                Picasso.with(Movie_Details.this).load(image_url + profile3).into
//                                        (image3);
//                            } else {
//
//                                image3.setVisibility(View.GONE);
//                            }
//
//                            for (int i = 0; i < image.length(); i++) {
//
//                                JSONObject obj = image.getJSONObject(i);
//                                String im = obj.getString("file_path");
//                                more_image_array.add(im);
//
//                            }
//
//                            JSONArray image11 = jsonObject.getJSONArray("posters");
//
//                            for (int i = 0; i < image11.length(); i++) {
//
//                                JSONObject obj = image11.getJSONObject(i);
//                                String im = obj.getString("file_path");
//                                more_image_array.add(im);
//
//                            }
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
//        requestQueue.add(request3);
//
//        JsonObjectRequest request4 = new JsonObjectRequest(Request.Method.GET,
//                urlPreId + id + similar_post, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        if (jsonObject == null || jsonObject.length() == 0) {
//                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//
//                        try {
//
//                            JSONArray arrayMovies = jsonObject.getJSONArray("results");
//
//
//                            String name1 = arrayMovies.getJSONObject(0).getString("title");
//                            String profile1 = arrayMovies.getJSONObject(0).getString
//                                    ("poster_path");
//                            sid1 = arrayMovies.getJSONObject(0).getString("id");
//                            if (name1 != null && name1.length() != 0) {
//                                similar_text1.setVisibility(View.VISIBLE);
//                                similarImage1.setVisibility(View.VISIBLE);
//                                similar_text1.setText(name1);
//                                Picasso.with(Movie_Details.this).load(image_url + profile1).into
//                                        (similarImage1);
//                            } else {
//
//                                similar_text1.setVisibility(View.GONE);
//                                similarImage1.setVisibility(View.GONE);
//                            }
//
//
//                            String name2 = arrayMovies.getJSONObject(1).getString("title");
//                            String profile2 = arrayMovies.getJSONObject(1).getString
//                                    ("poster_path");
//                            sid2 = arrayMovies.getJSONObject(1).getString("id");
//                            if (name2 != null && name2.length() != 0) {
//                                similar_text2.setVisibility(View.VISIBLE);
//                                similarImage2.setVisibility(View.VISIBLE);
//                                similar_text2.setText(name2);
//                                Picasso.with(Movie_Details.this).load(image_url + profile2).into
//                                        (similarImage2);
//                            } else {
//
//                                similar_text2.setVisibility(View.GONE);
//                                similarImage2.setVisibility(View.GONE);
//                            }
//
//
//                            String name3 = arrayMovies.getJSONObject(2).getString("title");
//                            String profile3 = arrayMovies.getJSONObject(2).getString
//                                    ("poster_path");
//                            sid3 = arrayMovies.getJSONObject(2).getString("id");
//                            if (name3 != null && name3.length() != 0) {
//                                similar_text3.setVisibility(View.VISIBLE);
//                                similarImage3.setVisibility(View.VISIBLE);
//                                similar_text3.setText(name3);
//                                Picasso.with(Movie_Details.this).load(image_url + profile3).into
//                                        (similarImage3);
//                            } else {
//
//                                similar_text3.setVisibility(View.GONE);
//                                similarImage3.setVisibility(View.GONE);
//                            }
//
//
//                            for (int i = 0; i < arrayMovies.length(); i++) {
//                                long id = -1;
//                                String title = "NA";
//                                String releaseDate = "NA";
//                                int audienceScore = -1;
//                                String synopsis = "NA";
//                                String urlThumbnail = "NA";
//
//                                JSONObject currentmovie = arrayMovies.getJSONObject(i);
//                                title = currentmovie.getString("title");
//                                id = currentmovie.getLong("id");
//                                releaseDate = currentmovie.getString("release_date");
//                                audienceScore = currentmovie.getInt("vote_average");
//                                synopsis = currentmovie.getString("overview");
//                                urlThumbnail = currentmovie.getString("backdrop_path");
//
//
//                                Movie movie = new Movie(id, title, releaseDate, audienceScore, synopsis, urlThumbnail);
//
//                                if (id != -1 && !title.equals("NA")) {
//                                    similar_list.add(movie);
//                                }
//                            }
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
//        requestQueue.add(request4);
//
//        JsonObjectRequest request5 = new JsonObjectRequest(Request.Method.GET,
//                urlPreId + id + reviews_post, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        if (jsonObject == null || jsonObject.length() == 0) {
//                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//
//                        try {
//
//
//                            JSONArray rvw = jsonObject.getJSONArray("results");
//
//                            for (int i = 0; i < rvw.length(); i++) {
//
//                                JSONObject current = rvw.getJSONObject(i);
//                                String author = current.getString("author");
//                                String text = current.getString("content");
//
//                                Movie movie = new Movie(author, text);
//                                reviews.add(movie);
//
//                            }
//
//
//                        } catch (Exception e) {
//                            Toast.makeText(Movie_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();
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
//                        Toast.makeText(Movie_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();
//
//                    }
//                });
//
//        requestQueue.add(request5);


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
                if (cid1 != null && cid1.length() > 0) {
                    Intent intent1 = new Intent(Movie_Details.this, Person_Details.class);
                    intent1.putExtra("tv", cid1);
                    startActivity(intent1);
                }
            }
        });

        castImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cid2 != null && cid2.length() > 0) {
                    Intent intent1 = new Intent(Movie_Details.this, Person_Details.class);
                    intent1.putExtra("tv", cid2);
                    startActivity(intent1);
                }
            }
        });

        castImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cid3 != null && cid3.length() > 0) {
                    Intent intent1 = new Intent(Movie_Details.this, Person_Details.class);
                    intent1.putExtra("tv", cid3);
                    startActivity(intent1);
                }
            }
        });

        similarImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sid1 != null && sid1.length() > 0) {
                    Intent intent1 = new Intent(Movie_Details.this, Movie_Details.class);
                    intent1.putExtra("tv", sid1);
                    startActivity(intent1);
                }
            }
        });

        similarImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sid2 != null && sid2.length() > 0) {
                    Intent intent1 = new Intent(Movie_Details.this, Movie_Details.class);
                    intent1.putExtra("tv", sid2);
                    startActivity(intent1);
                }
            }
        });

        similarImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sid3 != null && sid3.length() > 0) {
                    Intent intent1 = new Intent(Movie_Details.this, Movie_Details.class);
                    intent1.putExtra("tv", sid3);
                    startActivity(intent1);
                }
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
                    new SnackBar.Builder(Movie_Details.this)
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

                        new SnackBar.Builder(Movie_Details.this)
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
                dbMovies = new DBMovies(Movie_Details.this);
                final boolean boo2 = dbMovies.checkWatch(w_id);
                boolean bool = dbMovies.checkWish(w_id);
                if (bool) {

                    new SnackBar.Builder(Movie_Details.this)
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
                        new SnackBar.Builder(Movie_Details.this)
                                .withMessage("Added to Wish List") // OR
                                .withTextColorId(R.color.accent_color)
                                .withBackgroundColorId(R.color.primaryColor)
                                .show();

                    }
                }
            }
        });

        // worksOnWatchWish(w_id,w_name);

        final OnPublishListener onPublishListener = new OnPublishListener() {
            @Override
            public void onComplete(String postId) {
                //Log.i(TAG, "Published successfully. The new post id = " + postId);
            }
        };


        final Feed feed1 = new Feed.Builder()
                .setMessage("Watching")
                .setName(movie_name)
                .setCaption("Enjoying")
                .setDescription(description)
                .setPicture(image_link)
//                .setLink(movie_link)
//                .addAction("", movie_link)
//                .addProperty("Full documentation", "https://github.com/razon30")
//                .addProperty("Stars", "14")
                .build();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                layout2.setVisibility(View.VISIBLE);
//                layout3.setVisibility(View.VISIBLE);

                mSimpleFacebook.publish(feed1, true, onPublishListener);

            }
        });

    }

    private void LoadingKajKarbar() {

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

//                                Picasso.with(Movie_Details.this).load(image_url + backdrop_path).into
//                                        (coverLayout1);

                            } else {
                                Picasso.with(Movie_Details.this).load(R.drawable.ic_launcher).into
                                        (coverLayout);
                                coverLayout.setImageResource(R.drawable.ic_launcher);


                            }

                            image_link = image_url + backdrop_path;


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

                            description = String.valueOf(sb);

                            homepage = "";
                            homepage = jsonObject.getString("homepage");
                            if (homepage != "" && homepage != null) {
                                tvHomepage.setText("Home Page:  " + homepage);
                            } else {
                                tvHomepage.setText("Home Page:  " + "NA");
                            }

                            movie_link = homepage;

                            String imdb_id = "";
                            imdb_details_url_id = jsonObject.getString("imdb_id");
                            if (imdb_details_url_id != "" && imdb_details_url_id != null) {
                                tvImbdId.setText(imdb_details_url_id);

                            } else {
                                tvImbdId.setText("NA");
                            }

                            worksOnRating(imdb_details_url_id);


//                            String vote = "";
//                            vote = jsonObject.getString("vote_count");
//                            if (vote != null) {
//                                tvVotenumber.setText(vote);
//                            } else {
//                                tvVotenumber.setText("");
//                            }
//                            double audience_score = -1;
//
//                            audience_score = jsonObject.getDouble("vote_average");
//
//                            if (audience_score == -1) {
//                                tvRating.setText(audience_score + "");
//
//                            } else {
//                                tvRating.setText(audience_score + "");
//
//                            }

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
                            movie_name = title + "  (" + date[0] + ")";
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

                            if (revenue != null && revenue.length() != 0 && revenue != "" &&
                                    revenue != "0") {
                                tvRevenue.setText("$" + revenue);
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


                        } catch (Exception e) {


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

        requestQueue.add(request);

//        JsonObjectRequest request_imdb = new JsonObjectRequest(Request
//                .Method.GET, imdb_details_url_pre + imdb_details_url_id + imdb_details_url_post, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        if (jsonObject == null || jsonObject.length() == 0) {
//                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//
//                        try {
//
//                            String vote = "";
//                            vote = jsonObject.getString("imdbVotes");
//                            if (vote != null) {
//                                tvVotenumber.setText(vote);
//                            } else {
//                                tvVotenumber.setText("");
//                            }
//                            double audience_score = -1;
//
//                            audience_score = jsonObject.getDouble("imdbRating");
//
//                            if (audience_score == -1) {
//                                tvRating.setText(audience_score + "");
//
//                            } else {
//                                tvRating.setText(audience_score + "");
//
//                            }
//
//
//
//                        } catch (Exception e) {
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
//
//                    }
//                });
//
//        requestQueue.add(request_imdb);


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


                            JSONArray image_slide = jsonObject.getJSONArray("backdrops");

                            String[] arrayList = new String[image_slide.length()];

                            for (int i = 0; i < image_slide.length(); i++) {

                                JSONObject obj = image_slide.getJSONObject(i);
                                String im = obj.getString("file_path");
                                more_image_array.add(im);

                            }

//                            adapter = new PicassoBitmapAdapter(Movie_Details.this, Arrays.asList
//                                    (arrayList));
//                            slideShowView.setAdapter(adapter);
//                            slideShowView.play();

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

                            String profile3 = image.getJSONObject(0).getString("file_path");
                            if (profile3 != null && profile3.length() != 0) {
                                image3.setVisibility(View.VISIBLE);
                                Picasso.with(Movie_Details.this).load(image_url + profile3).into
                                        (image3);
                            } else {

                                image3.setVisibility(View.GONE);
                            }

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


    }

    private void worksOnRating(String imdb_details_url_id) {
        JsonObjectRequest request_imdb = new JsonObjectRequest(Request
                .Method.GET, "http://www.omdbapi.com/?i=" + imdb_details_url_id + "&plot=full&r=json", null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Movie_Details.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            String vote = "";
                            vote = jsonObject.getString("imdbVotes");
                            if (vote != null) {
                                if (vote.length() > 5) {
                                    tvVotenumber.setTextSize(30);
                                }
                                tvVotenumber.setText(vote);
                            } else {
                                tvVotenumber.setText("");
                            }
                            String audience_score = "-1";

                            audience_score = jsonObject.getString("imdbRating");

                            if (audience_score == "-1") {
                                tvRating.setText(audience_score + "");

                            } else {
                                if (audience_score.length() > 1) {
                                    tvRating.setTextSize(30);
                                }
                                tvRating.setText(audience_score);

                            }

                            String award = jsonObject.getString("Awards");
                            tvAwards.setText(award);


                        } catch (Exception e) {


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

        requestQueue.add(request_imdb);
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

                dbMovies = new DBMovies(Movie_Details.this);

                if (item.getItemId() == R.id.action_search) {
                    openSearch();
                }

                if (item.getItemId() == R.id.action_share) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Watching " + movie_name + "\n" + movie_link);
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent, "Watching " + movie_name));

                }

                if (item.getItemId() == R.id.refresh) {
                    Intent intent = new Intent(Movie_Details.this, Movie_Details.class);
                    intent.putExtra("tv", w_id);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.clearWatch) {

                    dbMovies.deleteAllWatch();
                    Toast.makeText(Movie_Details.this, "Watch List cleared", Toast.LENGTH_LONG).show();
                }
                if (item.getItemId() == R.id.clearWish) {
                    dbMovies.deleteAllWish();
                    Toast.makeText(Movie_Details.this, "Wish List cleared", Toast.LENGTH_LONG).show();
                }
                if (item.getItemId() == R.id.about) {
                    Intent intent = new Intent(Movie_Details.this, Credit.class);
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
                    Movie_Details.this);

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


    private void worksOnWatchWish(final String w_id, final String w_name) {


    }

    private void initualizing_contents() {

        //  slideShowView = (SlideShowView) findViewById(R.id.slideshow);


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
        tvAwards = (TextView) findViewById(R.id.award_details);
//        Picasso.with(Movie_Details.this).load(R.drawable.bookmark_toolbar).resize(70, 70)
//                .into(imageRating);

        btn_moreImage = (TextView) findViewById(R.id.more_image);
        btn_reviws = (TextView) findViewById(R.id.review_details);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "roboto_slab_regular.ttf");
        btn_reviws.setTypeface(custom_font);

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

                    Intent intent = new Intent(Movie_Details.this, Multi_Search_Activity.class);
                    intent.putExtra("tv", key);
                    startActivity(intent);

                } else {
                    Toast.makeText(Movie_Details.this, "Not Proper Keyword", Toast.LENGTH_LONG).show();
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

        //callbackManager.onActivityResult(requestCode, resultCode, data);
        mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
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
            // add.setBackgroundColor(getResources().getColor(R.color.Style_one_toolbar));
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
            // add.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
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
            // add.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
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
            // add.setBackgroundColor(getResources().getColor(R.color.Style_four_toolbar));
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
            // add.setBackgroundColor(getResources().getColor(R.color.Style_five_toolbar));
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
            // add.setBackgroundColor(getResources().getColor(R.color.Style_six_toolbar));
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
            // add.setBackgroundColor(getResources().getColor(R.color.Style_seven_toolbar));
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
            //  add.setBackgroundColor(getResources().getColor(R.color.Style_eight_toolbar));
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
            //  add.setBackgroundColor(getResources().getColor(R.color.Style_nine_toolbar));
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
            // add.setBackgroundColor(getResources().getColor(R.color.Style_ten_toolbar));
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
            // add.setBackgroundColor(getResources().getColor(R.color.Style_eleven_view));
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

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(Movie_Details.this, MainActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onResume() {
        super.onResume();
        mSimpleFacebook = SimpleFacebook.getInstance(this);
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

    public class MyAsyncTask extends AsyncTask<Void, Void, Boolean> {

        // LoadToast lt;

        Context context;
        SpotsDialog dialog;

        private ProgressDialog dialog1;
        //  SweetAlertDialog sweetAlertDialog;

        public MyAsyncTask(Context context) {

            this.context = context;
            dialog = new SpotsDialog(context, R.style.Custom);

//            dialog1 = new ProgressDialog(context);
//            lt = new LoadToast(context);
//            sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE);

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            this.dialog1.setMessage("Progress start");
//            this.dialog1.show();


//           sweetAlertDialog
//                    .setTitleText("Good job!")
//                    .setContentText("You clicked the button!")
//                    .show();
//
//            lt = new LoadToast(Movie_Details.this);
//            lt.setTranslationY(1000);
//            lt.setBackgroundColor(R.color.background2);
//            lt.setProgressColor(R.color.accentColor);
//            lt.setTextColor(R.color.accent_color);
//            lt.setText("Loading").show();

//
            dialog.setMessage("Loading");
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            LoadingKajKarbar();

            try {
                Thread.sleep(3000);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            //  lt.success();

//            if (dialog1.isShowing()) {
//                dialog1.dismiss();
//            }

            if (aVoid) {
                // lt.success();
                dialog.cancel();

            }


        }
    }


}
