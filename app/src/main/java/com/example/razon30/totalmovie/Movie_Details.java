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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.etiennelawlor.imagegallery.library.activities.ImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.enums.PaletteColorType;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.github.mrengineer13.snackbar.SnackBar;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import dmax.dialog.SpotsDialog;


public class Movie_Details extends AppCompatActivity {

    public ArrayList<Movie> cast_and_crew = new ArrayList<Movie>();
    //similar
    public ArrayList<Movie> similar_list = new ArrayList<Movie>();
    //reviews
    public ArrayList<Movie> reviews = new ArrayList<Movie>();

    ImageView coverLayout;
    CircularImageView circularImageView, ratingCircularImageView1;
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
    String imdb_details_url_id;

    String trailer, homepage;
    ArrayList<String> more_image_array = new ArrayList<String>();

    ImageView image1, image2, image3;
    String backdrop1, backdrop2, backdrop3;

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
    Button watch;
    ImageView wish;
    //ImageView add;
    TextView tvGenre;
    //for share on facebook
    String movie_name = "", description = "", image_link = "", movie_link = "";
    String shareUrl = "";
    String about = " ";
    ArrayList<String> searchList;
    private ImageLoader imageLoader;
    //Retriving data
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private SearchBox search;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_movie__details);

        searchList = new ArrayList<>();
        searchList.add("abcdefgh");
        searchList.add("ijklmn");
        searchList.add("nopqr");
        searchList.add("stuv");
        searchList.add("wxyz");

        final Intent intent = getIntent();
        id = Long.parseLong(intent.getStringExtra("tv"));
        String shareUrl_demo = intent.getStringExtra("url");

        if (shareUrl_demo != null && shareUrl_demo.length() != 0) {
            shareUrl = shareUrl_demo;
        }

        w_id = String.valueOf(id);
        initualizing_contents();
        worksOncolor();
        worksOnNetwork();
        worksOnSearch(w_id);


        dbMovies = new DBMovies(Movie_Details.this);
        // layout1 = (LinearLayout) findViewById(R.id.multiple_layout);
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



        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();


        MyAsyncTask task = new MyAsyncTask(Movie_Details.this);
        task.execute();

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

                        Movie movie = cast_and_crew.get(position);
                        String id = String.valueOf(movie.getId());

                        Intent intent = new Intent(Movie_Details.this, Person_Details.class);
                        intent.putExtra("tv", id);


                        startActivity(intent);


                    }

                    @Override
                    public void onLongClick(View v, int position) {

                    }
                }));


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        Movie_Details.this);

                if (cast_and_crew == null || cast_and_crew.size() == 0) {
                    new SnackBar.Builder(Movie_Details.this)
                            .withMessage("No Cast and Crew Found") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
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
                    new SnackBar.Builder(Movie_Details.this)
                            .withMessage("No Link is Available") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
                }
            }
        });

        btn_moreImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (more_image_array == null || more_image_array.size() == 0) {
                    new SnackBar.Builder(Movie_Details.this)
                            .withMessage("No Image Found or Network Error") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
                    return;
                } else {



                    Intent intent = new Intent(Movie_Details.this, ImageGalleryActivity.class);
                    // Intent intent = new Intent(MainActivity.this, ImageGalleryActivity.class);

//
                    intent.putStringArrayListExtra("images", more_image_array);

                    intent.putExtra("palette_color_type", PaletteColorType.VIBRANT);

                    startActivity(intent);


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
                        String image = image_url + movie.getUrlThumbnail();

                        Intent intent = new Intent(Movie_Details.this, Movie_Details.class);
                        intent.putExtra("tv", id);
                        intent.putExtra("url", image);


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
                    new SnackBar.Builder(Movie_Details.this)
                            .withMessage("No Similar Movie Found") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
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
                    new SnackBar.Builder(Movie_Details.this)
                            .withMessage("No Reviews Found") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
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
                    intent1.putExtra("url", backdrop1);
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
                    intent1.putExtra("url", backdrop2);
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
                    intent1.putExtra("url", backdrop3);
                    startActivity(intent1);
                }
            }
        });

//Toast.makeText(Movie_Details.this,w_name,Toast.LENGTH_LONG).show();

//        Bitmap img = BitmapFactory.decodeResource(getResources(), );
//        img = BitmapFactory.

        final View view = getLayoutInflater().inflate(R.layout.aboutshare, null);
        final TextView remove = (TextView) view.findViewById(R.id.remove);
        final ShareButton shareButton = (ShareButton) view.findViewById(R.id.okShare);
        final TextView tag = (TextView) view.findViewById(R.id.fbAbout);

        final ShareLinkContent content = new ShareLinkContent.Builder()
                //.setContentUrl(Uri.parse("http://t1.gstatic" +
                // ".com/images?q=tbn:ANd9GcQm-J_KZ9tj4WTHJY7jYGKtDHsMYa4OBvTJJ6VaGzIW7Xvg7snQYA"))
                .setContentUrl(Uri.parse(shareUrl))
                .setContentTitle("Watching")
                .setContentDescription("Amazing")
                        //.setContentDescription(editText.getText().toString())
                .build();



        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMovies = new DBMovies(Movie_Details.this);
                if (shareUrl == null || shareUrl.length() == 0) {

                    new SnackBar.Builder(Movie_Details.this)
                            .withMessage("Sorry,No link is Available to share") // OR
                            .withTextColorId(R.color.accent_color)
                            .withBackgroundColorId(R.color.primaryColor)
                            .show();
                }

                boolean bool = dbMovies.checkWatch(w_id);
                final boolean boo2 = dbMovies.checkWish(w_id);
                if (bool) {


                    tag.setText("Already in Watch List");
                    remove.setVisibility(View.VISIBLE);
                    final DialogPlus dialog = DialogPlus.newDialog(Movie_Details.this)

                            .setContentHolder(new ViewHolder(view))
                            .setExpanded(true)  // This will enable the expand feature, (similar to
                                    // android L share dialog)
                            .setMargin(100, 5, 100, 5)
                            .setGravity(Gravity.CENTER)
                            .setCancelable(true)
                            .setInAnimation(R.anim.slide_in_bottom)
                            .setOutAnimation(R.anim.slide_out_bottom)
                            .create();
                    dialog.show();
                    shareButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            shareButton.setShareContent(content);
                        }
                    });

                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dbMovies.deleteWatch(w_id);
                            dialog.dismiss();
                            new SnackBar.Builder(Movie_Details.this)
                                    .withMessage("Removed Successfully") // OR
                                    .withTextColorId(R.color.accent_color)
                                    .withBackgroundColorId(R.color.primaryColor)
                                    .show();
                        }
                    });


                } else {
                    Movie movie = new Movie(w_id, w_name);
                    long fact = dbMovies.insertWatch(movie);
                    if (fact != -1) {
                        watch.setBackgroundResource(R.color.accent_color);


                        remove.setVisibility(View.GONE);

                        // watch.setShareContent(content);
                        tag.setText("Share On Facebook?");

                        final DialogPlus dialog = DialogPlus.newDialog(Movie_Details.this)

                                .setContentHolder(new ViewHolder(view))
                                .setExpanded(true)  // This will enable the expand feature, (similar to
                                        // android L share dialog)
                                .setMargin(100, 5, 100, 5)
                                .setGravity(Gravity.CENTER)
                                .setCancelable(true)
                                .setInAnimation(R.anim.slide_in_bottom)
                                .setOutAnimation(R.anim.slide_out_bottom)
                                .create();
                        dialog.show();
                        shareButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareButton.setShareContent(content);
                            }
                        });


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
                    Movie movie = new Movie(w_id, w_name, "null", "null");
                    long fact = dbMovies.insertWish(movie);
                    if (fact != -1) {
                        wish.setBackgroundResource(R.color.accent_color);
                        new SnackBar.Builder(Movie_Details.this)
                                .withMessage("Added to Wish List") // OR
                                .withTextColorId(R.color.accent_color)
                                .withBackgroundColorId(R.color.primaryColor)
                                .withActionMessage("Set Alarm")
                                .withOnClickListener(new SnackBar.OnMessageClickListener() {
                                    @Override
                                    public void onMessageClick(Parcelable token) {

                                        makeAlarm(w_id, w_name);

                                    }
                                })
                                .show();

                    }
                }
            }
        });

    }

    private void makeAlarm(String w_id, String w_name) {

        final String movie_ID = w_id;
        final String movie_NAME = w_name;

        View view1 = getLayoutInflater().inflate(R.layout.close_alarm,
                null);

        final DatePicker datePicker = (DatePicker) view1.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) view1.findViewById(R.id.timePicker);
        com.example.razon30.totalmovie.MyTextViewOne ok = (MyTextViewOne) view1.findViewById(R.id.setAlarm);
        // com.example.razon30.totalmovie.MyTextViewOne cancel = (MyTextViewOne) view1.findViewById
        //      (R.id.cancelAlarm);
        final com.example.razon30.totalmovie.MyTextViewOne dateTime = (MyTextViewOne) view1.findViewById(R.id.timeDate);

        if (datePicker.getVisibility() == View.GONE) {

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            dateTime.setText(date);

        }

        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePicker.getVisibility() == View.GONE) {
                    timePicker.setVisibility(View.GONE);
                    datePicker.setVisibility(View.VISIBLE);


                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                    Date currentLocalTime = cal.getTime();
                    DateFormat date = new SimpleDateFormat("HH:mm a");
// you can get seconds by adding  "...:ss" to it
                    date.setTimeZone(TimeZone.getTimeZone("GMT+6:00"));
                    String localTime = date.format(currentLocalTime);
                    dateTime.setText(localTime);

                } else if (timePicker.getVisibility() == View.GONE) {
                    timePicker.setVisibility(View.VISIBLE);
                    datePicker.setVisibility(View.GONE);
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                    dateTime.setText(date);

                }
            }
        });

        final AlertDialog builder = new AlertDialog.Builder(Movie_Details.this).create();
        builder.setView(view1);
        builder.show();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMovies.deleteWish(movie_ID);

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                String monthStr = chooseMonth(month);
                String date = day + " " + monthStr + " " + year;

                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                String time = setTime(hour, minute);

                Movie movie = new Movie(movie_ID, movie_NAME, time, date);
                dbMovies.insertWish(movie);
                builder.cancel();
                new SnackBar.Builder(Movie_Details.this)
                        .withMessage("Alarm is set") // OR
                        .withTextColorId(R.color.accent_color)
                        .withBackgroundColorId(R.color.primaryColor)
                        .show();
            }
        });

//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });


    }

    private String setTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        return aTime;
    }

    private String chooseMonth(int month) {

        if (month == 01 || month == 1) {
            return "Jan";
        } else if (month == 02 || month == 2) {
            return "Feb";
        } else if (month == 03 || month == 3) {
            return "March";
        } else if (month == 04 || month == 4) {
            return "April";
        } else if (month == 05 || month == 5) {
            return "May";
        } else if (month == 06 || month == 6) {
            return "June";
        } else if (month == 07 || month == 7) {
            return "July";
        } else if (month == 8) {
            return "Aug";
        } else if (month == 9) {
            return "Sep";
        } else if (month == 10) {
            return "Oct";
        } else if (month == 11) {
            return "Nov";
        } else if (month == 12) {
            return "Dec";
        }
        return "null";

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
                            new SnackBar.Builder(Movie_Details.this)
                                    .withMessage("Problem to Load") // OR
                                    .withTextColorId(R.color.translucent_black_light)
                                    .withBackgroundColorId(R.color.accent_color)
                                    .withTypeFace(Typeface.SANS_SERIF)
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
                            new SnackBar.Builder(Movie_Details.this)
                                    .withMessage("Problem to Load") // OR
                                    .withTextColorId(R.color.translucent_black_light)
                                    .withBackgroundColorId(R.color.accent_color)
                                    .withTypeFace(Typeface.SANS_SERIF)
                                    .show();
                        }

                        try {


                            JSONArray image_slide = jsonObject.getJSONArray("backdrops");

                            String[] arrayList = new String[image_slide.length()];

                            for (int i = 0; i < image_slide.length(); i++) {

                                JSONObject obj = image_slide.getJSONObject(i);
                                String im = image_url + obj.getString("file_path");
                                more_image_array.add(im);

                            }


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
                                String im = image_url + obj.getString("file_path");
                                more_image_array.add(im);

                            }



                        } catch (Exception e) {



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
                            new SnackBar.Builder(Movie_Details.this)
                                    .withMessage("Problem to Load") // OR
                                    .withTextColorId(R.color.translucent_black_light)
                                    .withBackgroundColorId(R.color.accent_color)
                                    .withTypeFace(Typeface.SANS_SERIF)
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
                                backdrop1 = image_url + profile1;

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
                                backdrop2 = image_url + profile2;
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
                                backdrop3 = image_url + profile3;
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
                            new SnackBar.Builder(Movie_Details.this)
                                    .withMessage("Problem to Load") // OR
                                    .withTextColorId(R.color.translucent_black_light)
                                    .withBackgroundColorId(R.color.accent_color)
                                    .withTypeFace(Typeface.SANS_SERIF)
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
                            new SnackBar.Builder(Movie_Details.this)
                                    .withMessage("Problem to Load") // OR
                                    .withTextColorId(R.color.translucent_black_light)
                                    .withBackgroundColorId(R.color.accent_color)
                                    .withTypeFace(Typeface.SANS_SERIF)
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
                    // openSearch();
                    Intent intent = new Intent(Movie_Details.this, SearchActivity.class);
                    startActivity(intent);
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
                    new SnackBar.Builder(Movie_Details.this)
                            .withMessage("Watch List Cleared")
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
                }
                if (item.getItemId() == R.id.clearWish) {
                    dbMovies.deleteAllWish();
                    new SnackBar.Builder(Movie_Details.this)
                            .withMessage("Wish List Cleared")
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
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

        rating_card = (CardView) findViewById(R.id.rating_card);

        coverLayout = (ImageView) findViewById(R.id.cover);

        circularImageView = (CircularImageView) findViewById(R.id.play_trailer);
        ratingCircularImageView1 = (CircularImageView) findViewById(R.id.image_rating);
        imageView = (ImageView) findViewById(R.id.postar_image_detail);
        button = (TextView) findViewById(R.id.cast_and_crew);

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
        watch = (Button) findViewById(R.id.watch);
        wish = (ImageView) findViewById(R.id.wish);
        //add = (ImageView) findViewById(R.id.multiple_actions);
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
        for (int i = 0; i < searchList.size(); i++) {
            SearchResult options = new SearchResult(searchList.get(i), getResources().getDrawable(R
                    .drawable.abc_ic_go_search_api_mtrl_alpha));
            search.addSearchable(options);
        }
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
                // search.clearSearchable();

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
                    new SnackBar.Builder(Movie_Details.this)
                            .withMessage("Not Proper Keyword") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
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
        //mSimpleFacebook.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        search.clearSearchable();
        search.clearResults();
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
            ratingCircularImageView1.setBackgroundColor(getResources().getColor(R.color.Style_one_navigationBar));



            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.Style_one_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color.Style_one_navigationBar));
            }
        }
        if (i == 2) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_two_toolbar));
            // add.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_two_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_two_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_two_toolbar));
            ratingCircularImageView1.setBackgroundColor(getResources().getColor(R.color
                    .Style_two_navigationBar));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color
                        .Style_two_navigationBar));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_two_navigationBar));
            }
        }
        if (i == 3) {

            tvGenre.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            // add.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            wish.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            watch.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            rating_card.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            ratingCircularImageView1.setBackgroundColor(getResources().getColor(R.color
                    .Style_three_navigationBar));
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
            ratingCircularImageView1.setBackgroundColor(getResources().getColor(R.color
                    .Style_four_navigationBar));
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
            ratingCircularImageView1.setBackgroundColor(getResources().getColor(R.color
                    .Style_five_navigationBar));
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
            ratingCircularImageView1.setBackgroundColor(getResources().getColor(R.color
                    .Style_six_navigationBar));
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
            ratingCircularImageView1.setBackgroundColor(getResources().getColor(R.color
                    .Style_seven_navigationBar));
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

    @Override
    public void onResume() {
        super.onResume();
        //mSimpleFacebook = SimpleFacebook.getInstance(this);
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();

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

    public class MyAsyncTask extends AsyncTask<Void, Void, Boolean> {

        // LoadToast lt;

        Context context;
        SpotsDialog dialog;

        private ProgressDialog dialog1;
        //  SweetAlertDialog sweetAlertDialog;

        public MyAsyncTask(Context context) {

            this.context = context;
            dialog = new SpotsDialog(context, R.style.Custom);


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading..");
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

            if (aVoid) {
                // lt.success();
                dialog.cancel();

            }


        }
    }
}
