package com.example.razon30.totalmovie;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.mrengineer13.snackbar.SnackBar;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.quinny898.library.persistentsearch.SearchBox;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import io.karim.MaterialTabs;
import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //remembering drawer was seen by user or not
    private static final String FIRST_TIME = "first_time";
    //sorting
    private static final int JOB_ID = 100;
    //for drawer
    NavigationView mDrawer;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    //for drawer header
    ImageView drawer_cover_image;
    CircularImageView drawer_profile_image;
    TextView drawer_name, drawer_profile;
    CallbackManager callbackManager;
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    String profile_link;
    RelativeLayout imageCoverLayout;
    boolean login = false;
    //for drawer menu
    // LinearLayout drawer_discover, drawer_box, drawer_upcoming, drawer_watch, drawer_wish;
    DBMovies dbMovies;
    EditText etSearch;
    Button btnSearch;
    String urlPre = "http://api.themoviedb.org/3/search/";
    String multiPost = "multi?api_key=f246d5e5105e9934d3cd4c4c181d618d&query=";
    String keyword = "";
    SharedPreferences sharedPreferences;
    View view;
    //for tab
    MaterialTabs tabHost;
    JobScheduler jobScheduler;
    //for search
    private SearchBox search;
    private Toolbar toolbar;
    private boolean mUserSawDrawer = false;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        imageCoverLayout = (RelativeLayout) findViewById(R.id.imageHeaderLayout);

        drawer_cover_image = (ImageView) findViewById(R.id.fb_cover_image);
        drawer_profile_image = (CircularImageView) findViewById(R.id.fb_profile_image);
        drawer_name = (TextView) findViewById(R.id.fb_profile_name);
        drawer_profile = (TextView) findViewById(R.id.fb_profile);

        jobScheduler = JobScheduler.getInstance(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(MainActivity.this,
                        TestJobService.class));
                builder.setPeriodic(1000)
                        .setPersisted(true)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
                jobScheduler.schedule(builder.build());
            }

        }, 1000);

        //progressDialouge();

        if (!isNetworkAvailable()) {

            AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                    MainActivity.this);

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


        sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this);
        //for search
        search = (SearchBox) findViewById(R.id.searchbox);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        view = findViewById(R.id.view);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        tabHost = (MaterialTabs) findViewById(R.id.material_tabs);
        viewPager.setAdapter(new MainActivityPagerAdapter(getSupportFragmentManager()));
        tabHost.setViewPager(viewPager);


        worksOnColor();


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                dbMovies = new DBMovies(MainActivity.this);

                if (item.getItemId() == R.id.action_search) {
                    // openSearch();
                    startActivity(new Intent(MainActivity.this, SearchActivity.class));
                }
                if (item.getItemId() == R.id.refresh) {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.clearWatch) {

                    dbMovies.deleteAllWatch();
                    new SnackBar.Builder(MainActivity.this)
                            .withMessage("Watch List Cleared") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
                }
                if (item.getItemId() == R.id.clearWish) {
                    dbMovies.deleteAllWish();
                    new SnackBar.Builder(MainActivity.this)
                            .withMessage("Wish List Cleared") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
                }
                if (item.getItemId() == R.id.about) {
                    Intent intent = new Intent(MainActivity.this, Credit.class);
                    startActivity(intent);
                }

                return true;
            }
        });

        worksOnDrawer();
        if (isNetworkAvailable()) {
            worksOnDrawerHeader();
        }
        worksOnDrawerMenu();

    }


    private void worksOnDrawerMenu() {

        dbMovies = new DBMovies(MainActivity.this);
//        drawer_discover = (LinearLayout) findViewById(R.id.drawer_layout_discover);
//        drawer_box = (LinearLayout) findViewById(R.id.drawer_layout_Box_Office);
//        drawer_upcoming = (LinearLayout) findViewById(R.id.drawer_layout_Upcoming);
//        drawer_watch = (LinearLayout) findViewById(R.id.drawer_layout_watchList);
//        drawer_wish = (LinearLayout) findViewById(R.id.drawer_layout_wishList);
//
//        drawer_discover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawer(GravityCompat.START);
//                viewPager.setCurrentItem(0);
//            }
//        });
//
//        drawer_box.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawer(GravityCompat.START);
//                viewPager.setCurrentItem(1);
//            }
//        });
//
//        drawer_upcoming.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawerLayout.closeDrawer(GravityCompat.START);
//                viewPager.setCurrentItem(2);
//            }
//        });
//
//        drawer_watch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                View view1 = getLayoutInflater().inflate(R.layout.watch_wish_list,
//                        null);
//                ListView listView = (ListView) view1.findViewById(R.id.watch_list);
//                final ArrayList<Movie> watch_list = dbMovies.searchWatch();
//                BaseAdapter adapter = new BaseAdapter() {
//                    @Override
//                    public int getCount() {
//                        return watch_list.size();
//                    }
//
//                    @Override
//                    public Object getItem(int position) {
//                        return watch_list.get(position);
//                    }
//
//                    @Override
//                    public long getItemId(int position) {
//                        return position;
//                    }
//
//                    @Override
//                    public View getView(int position, View view, ViewGroup parent) {
//
//                        view = getLayoutInflater().inflate(R.layout.watch_wish_item,
//                                null);
//                        TextView textView = (TextView) view.findViewById(R.id.watchwltv);
//
//                        textView.setText(watch_list.get(position).getText());
//
//                        return view;
//                    }
//                };
//
//                listView.setAdapter(adapter);
//
//
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String m_id = watch_list.get(position).getAuthor();
//                        // String link = movie.getVideoURL();
//
//
//                        if (m_id != null && m_id.length() != 0) {
//                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
//                            Intent intent = new Intent(MainActivity.this, Movie_Details.class);
//                            intent.putExtra("tv", m_id);
//                            startActivity(intent);
//                        } else {
//                            return;
//                        }
//
//                    }
//                });
//
//
//                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
//                        MainActivity.this);
//
//                if (watch_list == null || watch_list.size() == 0) {
//                    Toast.makeText(MainActivity.this, watch_list.toString() + "No Watch List " +
//                            "found", Toast
//                            .LENGTH_LONG).show();
//
//                    return;
//                } else {
//
//                    builderAlertDialog
//                            .setView(view1)
//                            .show();
//                }
//
//
//            }
//        });
//
//
//        drawer_wish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                View view1 = getLayoutInflater().inflate(R.layout.watch_wish_list,
//                        null);
//                ListView listView = (ListView) view1.findViewById(R.id.watch_list);
//                final ArrayList<Movie> watch_list = dbMovies.searchWish();
//                BaseAdapter adapter = new BaseAdapter() {
//                    @Override
//                    public int getCount() {
//                        return watch_list.size();
//                    }
//
//                    @Override
//                    public Object getItem(int position) {
//                        return watch_list.get(position);
//                    }
//
//                    @Override
//                    public long getItemId(int position) {
//                        return position;
//                    }
//
//                    @Override
//                    public View getView(int position, View view, ViewGroup parent) {
//
//                        view = getLayoutInflater().inflate(R.layout.watch_wish_item,
//                                null);
//
//                        TextView textView = (TextView) view.findViewById(R.id.watchwltv);
//
//                        textView.setText(watch_list.get(position).getText());
//
//                        return view;
//                    }
//                };
//
//                listView.setAdapter(adapter);
//
//
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String m_id = watch_list.get(position).getAuthor();
//                        // String link = movie.getVideoURL();
//
//
//                        if (m_id != null && m_id.length() != 0) {
//                            // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
//                            Intent intent = new Intent(MainActivity.this, Movie_Details.class);
//                            intent.putExtra("tv", m_id);
//                            startActivity(intent);
//                        } else {
//                            return;
//                        }
//
//                    }
//                });
////                Toast.makeText(MainActivity.this, watch_list.get(0).getText() +"", Toast
////                        .LENGTH_LONG).show();
////
////
//
//                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
//                        MainActivity.this);
//
//                if (watch_list == null || watch_list.size() == 0) {
//                    Toast.makeText(MainActivity.this, watch_list.toString() + "No Wish List " +
//                            "found", Toast
//                            .LENGTH_LONG).show();
//
//                    return;
//                } else {
//
//                    builderAlertDialog
//                            .setView(view1)
//                            .show();
//                }
//
//
//            }
//        });


    }

    private void worksOnDrawerHeader() {

        if (Build.VERSION.SDK_INT >= 21) {

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList
                    ("public_profile", "user_friends"));
//
            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.example.razon30.totalmovie",
                        PackageManager.GET_SIGNATURES);
                for (android.content.pm.Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA1");
                    md.update(signature.toByteArray());
                    Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                    // Toast.makeText(MainActivity.this,"KeyHash: "+Base64.encodeToString(md.digest(),
                    //       Base64.DEFAULT),Toast.LENGTH_SHORT).show();
                }
            } catch (PackageManager.NameNotFoundException e) {

            } catch (NoSuchAlgorithmException e) {

            }

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            // App code
                            accessToken = loginResult.getAccessToken();
                            Profile profile = Profile.getCurrentProfile();
                            displayMessage(profile);
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            // App code
                        }
                    });


            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {


                }
            };

            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile1) {

                    displayMessage(profile1);

                }
            };

            accessTokenTracker.startTracking();
            profileTracker.startTracking();
        } else {
            drawer_cover_image.setImageDrawable(getResources().getDrawable(R.drawable.myrect));
            drawer_cover_image.setVisibility(View.GONE);
            drawer_name.setVisibility(View.GONE);
            drawer_profile.setVisibility(View.GONE);
            drawer_profile_image.setVisibility(View.GONE);
        }


    }

    private void displayMessage(Profile profile1) {

        if (Build.VERSION.SDK_INT >= 21) {

        //getting data from graph api
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Insert your code here
                        if (object != null) {
                            try {
                                String name = object.getString("name");
                                drawer_name.setText(name);
                                profile_link = object.getString("link");
                                JSONObject cover_obj = object.getJSONObject("cover");
                                String cover = cover_obj.getString("source");
                                Picasso.with(MainActivity.this).load(cover).into(drawer_cover_image);

                                JSONObject picture_obj = object.getJSONObject("picture");
                                JSONObject data_obj = picture_obj.getJSONObject("data");
                                String prof_pic = data_obj.getString("url");
                                Picasso.with(MainActivity.this).load(prof_pic).into(drawer_profile_image);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Log in To FaceBook", Toast
                                    .LENGTH_SHORT).show();
                        }


                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,cover,picture");
        request.setParameters(parameters);
        request.executeAsync();

        drawer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile_link != null && profile_link != "" && profile_link.length() != 0) {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(profile_link));
                    startActivity(i);
                } else {
                    new SnackBar.Builder(MainActivity.this)
                            .withMessage("No Link Is Available") // OR
                            .withTextColorId(R.color.translucent_black_light)
                            .withBackgroundColorId(R.color.accent_color)
                            .withTypeFace(Typeface.SANS_SERIF)
                            .show();
                }
            }
        });

        } else {
            drawer_cover_image.setImageDrawable(getResources().getDrawable(R.drawable.myrect));
            drawer_cover_image.setVisibility(View.GONE);
            drawer_name.setVisibility(View.GONE);
            drawer_profile.setVisibility(View.GONE);
            drawer_profile_image.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 21) {

            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
            // Logs 'install' and 'app activate' App Events.
            AppEventsLogger.activateApp(this);
        }
    }


    private void worksOnDrawer() {

        mDrawer = (NavigationView) findViewById(R.id.navigationView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R
                .string
                .open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mDrawer.setNavigationItemSelectedListener(this);


        //drawer 1st time or not
        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }


    }

    private void hideDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void markDrawerSeen() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();

    }

    private void showDrawer() {

        drawerLayout.openDrawer(GravityCompat.START);

    }

    private boolean didUserSeeDrawer() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        login = sharedPreferences.getBoolean("log", false);
        return mUserSawDrawer;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {


        if (menuItem.getItemId() == R.id.drawer_discover) {

            drawerLayout.closeDrawer(GravityCompat.START);
            viewPager.setCurrentItem(0);
        }
        if (menuItem.getItemId() == R.id.drawe_box_office) {

            drawerLayout.closeDrawer(GravityCompat.START);
            viewPager.setCurrentItem(1);
        }

        if (menuItem.getItemId() == R.id.drawer_upcomin) {

            drawerLayout.closeDrawer(GravityCompat.START);
            viewPager.setCurrentItem(2);

        }
        if (menuItem.getItemId() == R.id.drawer_watch_list) {
            dbMovies = new DBMovies(MainActivity.this);

            View view1 = getLayoutInflater().inflate(R.layout.watch_wish_list,
                    null);
            ListView listView = (ListView) view1.findViewById(R.id.watch_list);
            final ArrayList<Movie> watch_list = dbMovies.searchWatch();
            BaseAdapter adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return watch_list.size();
                }

                @Override
                public Object getItem(int position) {
                    return watch_list.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View view, ViewGroup parent) {

                    view = getLayoutInflater().inflate(R.layout.watch_wish_item,
                            null);
                    TextView textView = (TextView) view.findViewById(R.id.watchwltv);

                    textView.setText(watch_list.get(position).getText());

                    return view;
                }
            };

            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String m_id = watch_list.get(position).getAuthor();
                    // String link = movie.getVideoURL();


                    if (m_id != null && m_id.length() != 0) {
                        // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                        Intent intent = new Intent(MainActivity.this, Movie_Details.class);
                        intent.putExtra("tv", m_id);
                        startActivity(intent);
                    } else {
                        return;
                    }

                }
            });


            AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                    MainActivity.this);

            if (watch_list == null || watch_list.size() == 0) {
                new SnackBar.Builder(MainActivity.this)
                        .withMessage("No Watch List Found") // OR
                        .withTextColorId(R.color.translucent_black_light)
                        .withBackgroundColorId(R.color.accent_color)
                        .withTypeFace(Typeface.SANS_SERIF)
                        .show();

            } else {

                builderAlertDialog
                        .setView(view1)
                        .show();
            }
        }

        if (menuItem.getItemId() == R.id.drawer_wish_list) {

            dbMovies = new DBMovies(MainActivity.this);
            View view1 = getLayoutInflater().inflate(R.layout.watch_wish_list,
                    null);
            ListView listView = (ListView) view1.findViewById(R.id.watch_list);
            final ArrayList<Movie> watch_list = dbMovies.searchWish();
            BaseAdapter adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return watch_list.size();
                }

                @Override
                public Object getItem(int position) {
                    return watch_list.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View view, ViewGroup parent) {

                    view = getLayoutInflater().inflate(R.layout.watch_wish_item,
                            null);

                    TextView textView = (TextView) view.findViewById(R.id.watchwltv);
                    TextView time = (TextView) view.findViewById(R.id.timeoftimedetails);
                    TextView date = (TextView) view.findViewById(R.id.dateoftimedetails);

                    textView.setText(watch_list.get(position).getMovieNAME());
                    time.setText(watch_list.get(position).getAlarmTIME());
                    date.setText(watch_list.get(position).getAlarmDATE());

                    return view;
                }
            };

            listView.setAdapter(adapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String m_id = watch_list.get(position).getMovieID();
                    // String link = movie.getVideoURL();


                    if (m_id != null && m_id.length() != 0) {
                        // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
                        Intent intent = new Intent(MainActivity.this, Movie_Details.class);
                        intent.putExtra("tv", m_id);
                        startActivity(intent);
                    } else {
                        return;
                    }

                }
            });

            AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                    MainActivity.this);

            if (watch_list == null || watch_list.size() == 0) {
                new SnackBar.Builder(MainActivity.this)
                        .withMessage("No Wish List Found") // OR
                        .withTextColorId(R.color.translucent_black_light)
                        .withBackgroundColorId(R.color.accent_color)
                        .withTypeFace(Typeface.SANS_SERIF)
                        .show();

            } else {

                builderAlertDialog
                        .setView(view1)
                        .show();
            }
        }


        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openSearch() {
        toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);

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

                    Intent intent = new Intent(MainActivity.this, Multi_Search_Activity.class);
                    intent.putExtra("tv", key);
                    startActivity(intent);

                } else {
                    Toast.makeText(MainActivity.this, "Not Proper Keyword", Toast.LENGTH_LONG).show();
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

        callbackManager.onActivityResult(requestCode, resultCode, data);

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

    private void worksOnColor() {

        Random random = new Random();
        int i = random.nextInt(11 - 1 + 1) + 1;

        if (i == 1) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_one_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_one_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_one_tab));


            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color.Style_one_navigationBar));
            }
        }
        if (i == 2) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_two_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_two_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_two_tab));


            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_two_navigationBar));
            }
        }
        if (i == 3) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_three_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_three_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_three_tab));

            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_three_navigationBar));
            }
        }
        if (i == 4) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_four_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_four_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_four_tab));

            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_four_navigationBar));
            }
        }
        if (i == 5) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_five_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_five_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_five_tab));

            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_five_navigationBar));
            }
        }
        if (i == 6) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_six_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_six_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_six_tab));

            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_six_navigationBar));
            }
        }
        if (i == 7) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_seven_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_seven_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_seven_tab));

            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_seven_navigationBar));
            }
        }
        if (i == 8) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_eight_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_eight_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_eight_tab));

            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_eight_navigationBar));
            }
        }
        if (i == 9) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_nine_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_nine_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_nine_tab));

            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_nine_navigationBar));
            }
        }
        if (i == 10) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_ten_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_ten_toolbar));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_ten_tab));

            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color
                        .Style_ten_navigationBar));
            }
        }
        if (i == 11) {

            view.setBackgroundColor(getResources().getColor(R.color.Style_eleven_view));
            toolbar.setBackgroundColor(getResources().getColor(R.color.Style_eleven_view));
            tabHost.setBackgroundColor(getResources().getColor(R.color.Style_eleven_view));

            if (Build.VERSION.SDK_INT >= 21) {
                // getWindow().setStatusBarColor(getResources().getColor(R.color.accent_material_dark));
                getWindow().setNavigationBarColor(getResources().getColor(R.color.Style_eleven_view));
            }
        }

    }




    public class MainActivityPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Discover", "Box Office", "Upcoming"};


        public MainActivityPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                default:
                    return new FragmentSearch();
                case 1:
                    return new FragmentBoxOffice();
                case 2:
                    return new FragmentUpcoming();
            }
        }


    }

}
