package com.example.razon30.totalmovie;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Person_Details extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout rootLayout;

    ImageView image_cover,image_cover1, image_poster, hide, show;
    TextView name, biography, birth_date, birth_place, height, homepage, person_more_image, person_more_movies;
    Button popular_person;

    String urlPreId = "http://api.themoviedb.org/3/person/";
    long id;
    String urlLaterId = "?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String image_url = "http://image.tmdb.org/t/p/w500";
    String image_post = "/images?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String more_movie_post = "/movie_credits?api_key=f246d5e5105e9934d3cd4c4c181d618d";
    String person_popular_post = "popular?api_key=f246d5e5105e9934d3cd4c4c181d618d";

    //Retriving data
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    public ArrayList<Movie> cast_and_crew = new ArrayList<Movie>();

    //more image
    ListView oddList, evenList;
    ImageView more_image;
    ArrayList<String> oddArray = new ArrayList<String>();
    ArrayList<String> evenArray = new ArrayList<String>();
    ArrayList<String> more_image_array = new ArrayList<String>();

    //more movies
    public ArrayList<Movie> person_more_movie_list = new ArrayList<Movie>();

    //popular
    public ArrayList<Movie> popular_person_list = new ArrayList<Movie>();


    String person_homepage;
    int line_count;
    String profile_path;


    ImageView image1, image2, image3;

    ImageView movie_image1, movie_image2, movie_image3;
    TextView movie_name1, movie_name2, movie_name3;
    String mid1, mid2, mid3;

    TextView imagesPerson, moviesPerson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person__details);
        final Intent intent = getIntent();
        id = Long.parseLong(intent.getStringExtra("tv"));
        initialization();


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
                            Toast.makeText(Person_Details.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            String name1 = jsonObject.getString("name");
                            name.setText(name1);
                            collapsingToolbarLayout.setTitle(name1);

                            profile_path = "";
                            profile_path = jsonObject.getString("profile_path");

                            Picasso.with(Person_Details.this).load(image_url + profile_path).into
                                    (image_cover);
                            Picasso.with(Person_Details.this).load(image_url + profile_path).into
                                    (image_cover1);

                            Picasso.with(Person_Details.this).load(image_url + profile_path).into
                                    (image_poster);

                            person_homepage = "";
                            person_homepage = jsonObject.getString("homepage");
                            if (person_homepage != "" && person_homepage != null) {
                                homepage.setText(person_homepage);

                            } else {
                                homepage.setText("NA");
                            }


                            String overview = "";
                            overview = jsonObject.getString("biography");
                            if (overview != "" && overview != null) {
                                biography.setText("-''" + overview + "''");
                            } else {
                                biography.setText("NA");
                            }

                            line_count = biography.getLineCount();

                            if (line_count > 5) {

                                biography.setMaxLines(2);
                            }

                            String birthday = jsonObject.getString("birthday");
                            birth_date.setText(birthday);

                            String birthplace = jsonObject.getString("place_of_birth");
                            birth_place.setText(birthplace);


                        } catch (Exception e) {
//                            Toast.makeText(Person_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(Person_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request);

        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, urlPreId + id +
                image_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Person_Details.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {


                            JSONArray image = jsonObject.getJSONArray("profiles");

                            String profile1 = image.getJSONObject(0).getString("file_path");
                            if (profile1 != null && profile1.length() != 0) {
                                imagesPerson.setVisibility(imagesPerson.VISIBLE);
                                image1.setVisibility(image1.VISIBLE);
                                Picasso.with(Person_Details.this).load(image_url + profile1).into
                                        (image1);
                            } else {

                                imagesPerson.setVisibility(imagesPerson.GONE);
                                image1.setVisibility(image1.GONE);
                            }

                            String profile2 = image.getJSONObject(1).getString("file_path");
                            if (profile2 != null && profile2.length() != 0) {
                                imagesPerson.setVisibility(imagesPerson.VISIBLE);
                                image2.setVisibility(image2.VISIBLE);
                                Picasso.with(Person_Details.this).load(image_url + profile2).into
                                        (image2);
                            } else {

                                imagesPerson.setVisibility(imagesPerson.GONE);
                                image2.setVisibility(image2.GONE);
                            }


                            String profile3 = image.getJSONObject(2).getString("file_path");
                            if (profile3 != null && profile3.length() != 0) {
                                imagesPerson.setVisibility(imagesPerson.VISIBLE);
                                image3.setVisibility(image3.VISIBLE);
                                Picasso.with(Person_Details.this).load(image_url + profile3).into
                                        (image3);
                            } else {

                                imagesPerson.setVisibility(imagesPerson.GONE);
                                image3.setVisibility(image3.GONE);
                            }


                            for (int i = 0; i < image.length(); i++) {

                                JSONObject obj = image.getJSONObject(i);
                                String im = obj.getString("file_path");
                                more_image_array.add(im);

                            }

                            if (more_image_array.get(0) == null || more_image_array.get(0).length()
                                    == 0 || more_image_array.size() == 0) {
                                Picasso.with(Person_Details.this).load(image_url + profile_path).into
                                        (image_cover);
                            } else {

                                Picasso.with(Person_Details.this).load(image_url + more_image_array.get
                                        (0)).into
                                        (image_cover);

                            }

                            if (more_image_array.get(1) == null || more_image_array.get(1).length()
                                    == 0 || more_image_array.size() == 0) {
                                Picasso.with(Person_Details.this).load(image_url + profile_path).into
                                        (image_poster);
                            } else {
                                Picasso.with(Person_Details.this).load(image_url + more_image_array
                                        .get(1))
                                        .into
                                                (image_poster);


                            }


                        } catch (Exception e) {
//                            Toast.makeText(Person_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(Person_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request1);

        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, urlPreId + id +
                more_movie_post, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Person_Details.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray movies = jsonObject.getJSONArray("cast");


                            String name1 = movies.getJSONObject(0).getString("title");
                            String profile1 = movies.getJSONObject(0).getString("poster_path");
                            mid1 = movies.getJSONObject(0).getString("id");
                            if (name1 != null && name1.length() != 0) {
                                moviesPerson.setVisibility(moviesPerson.VISIBLE);
                                movie_name1.setVisibility(movie_name1.VISIBLE);
                                movie_image1.setVisibility(movie_image1.VISIBLE);
                                movie_name1.setText(name1);
                                Picasso.with(Person_Details.this).load(image_url + profile1).into
                                        (movie_image1);
                            } else {
                                moviesPerson.setVisibility(moviesPerson.GONE);
                                movie_name1.setVisibility(movie_name1.GONE);
                                movie_image1.setVisibility(movie_image1.GONE);
                            }


                            String name2 = movies.getJSONObject(1).getString("title");
                            String profile2 = movies.getJSONObject(1).getString("poster_path");
                            mid2 = movies.getJSONObject(1).getString("id");
                            if (name2 != null && name2.length() != 0) {
                                moviesPerson.setVisibility(moviesPerson.VISIBLE);
                                movie_name2.setVisibility(movie_name2.VISIBLE);
                                movie_image2.setVisibility(movie_image2.VISIBLE);
                                movie_name2.setText(name2);
                                Picasso.with(Person_Details.this).load(image_url + profile2).into
                                        (movie_image2);
                            } else {
                                moviesPerson.setVisibility(moviesPerson.GONE);
                                movie_name2.setVisibility(movie_name2.GONE);
                                movie_image2.setVisibility(movie_image2.GONE);
                            }


                            String name3 = movies.getJSONObject(2).getString("title");
                            String profile3 = movies.getJSONObject(2).getString("poster_path");
                            mid3 = movies.getJSONObject(2).getString("id");
                            if (name3 != null && name3.length() != 0) {
                                moviesPerson.setVisibility(moviesPerson.VISIBLE);
                                movie_name3.setVisibility(movie_name3.VISIBLE);
                                movie_image3.setVisibility(movie_image3.VISIBLE);
                                movie_name3.setText(name3);
                                Picasso.with(Person_Details.this).load(image_url + profile3).into
                                        (movie_image3);
                            } else {
                                moviesPerson.setVisibility(moviesPerson.GONE);
                                movie_name3.setVisibility(movie_name3.GONE);
                                movie_image3.setVisibility(movie_image3.GONE);
                            }





                            for (int i = 0; i < movies.length(); i++) {

                                JSONObject object = movies.getJSONObject(i);

                                String title = object.getString("title");
                                String character = object.getString("character");
                                String p_character = "as " + character;
                                String releaseDate = object.getString("release_date");
                                String poster = object.getString("poster_path");
                                long id = object.getLong("id");

                                Movie movie = new Movie(title, releaseDate, poster, p_character, id);
                                person_more_movie_list.add(movie);

                            }

                            JSONArray movies1 = jsonObject.getJSONArray("crew");

                            for (int i = 0; i < movies1.length(); i++) {

                                JSONObject object = movies1.getJSONObject(i);

                                String title = object.getString("title");
                                String character = object.getString("character");
                                String p_character = "as " + character;
                                String releaseDate = object.getString("release_date");
                                String poster = object.getString("poster_path");
                                long id = object.getLong("id");

                                Movie movie = new Movie(title, releaseDate, poster, p_character, id);
                                person_more_movie_list.add(movie);

                            }


                        } catch (Exception e) {
//                            Toast.makeText(Person_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(Person_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request2);

        JsonObjectRequest request3 = new JsonObjectRequest(Request.Method.GET, "http://api.themoviedb.org/3/person/popular?api_key=f246d5e5105e9934d3cd4c4c181d618d", null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        if (jsonObject == null || jsonObject.length() == 0) {
                            Toast.makeText(Person_Details.this, "Problem to load", Toast.LENGTH_LONG)
                                    .show();

                        }

                        try {

                            JSONArray results = jsonObject.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject current_result = results.getJSONObject(i);

                                String name = current_result.getString("name");
                                String profile_path = current_result.getString("profile_path");
                                long id = current_result.getLong("id");
                                String job = current_result.getString("popularity");

                                job = job.substring(0, 4);


                                Movie movie = new Movie(job, profile_path, id, name);
                                popular_person_list.add(movie);

                            }

                        } catch (Exception e) {
//                            Toast.makeText(Person_Details.this, e.toString(), Toast.LENGTH_LONG)
//                                    .show();
//

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(Person_Details.this, volleyError.toString(), Toast.LENGTH_LONG)
//                                .show();

                    }
                });

        requestQueue.add(request3);


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                biography.setMaxLines(line_count);
                show.setVisibility(show.GONE);
                hide.setVisibility(hide.VISIBLE);

            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                biography.setMaxLines(2);
                show.setVisibility(show.VISIBLE);
                hide.setVisibility(hide.GONE);

            }
        });

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (person_homepage != null && person_homepage != "" && person_homepage.length() != 0) {
                    Intent i = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(person_homepage));
                    startActivity(i);
                } else {
                    Toast.makeText(Person_Details.this, "No Link is Available", Toast.LENGTH_LONG)
                            .show();
                }


            }
        });

        person_more_image.setOnClickListener(new View.OnClickListener() {
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

                Adapter_more_image oddAdapter = new Adapter_more_image(oddArray, Person_Details.this);
                Adapter_more_image evenAdapter = new Adapter_more_image(evenArray, Person_Details.this);

                oddView.setAdapter(oddAdapter);
                evenView.setAdapter(evenAdapter);

                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        Person_Details.this);

                if (more_image_array == null || more_image_array.size() == 0) {
                    Toast.makeText(Person_Details.this, "No more image is Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();

                }

            }
        });

        person_more_movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view1 = getLayoutInflater().inflate(R.layout.review, null);
                ListView movieView = (ListView) view1.findViewById(R.id.review_list);

                Person_More_Movies_Adapter adapter = new Person_More_Movies_Adapter
                        (person_more_movie_list, Person_Details.this);

                movieView.setAdapter(adapter);

                movieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Movie current = person_more_movie_list.get(position);
                        String movie_id = String.valueOf(current.getId());

                        Intent intent1 = new Intent(Person_Details.this, Movie_Details.class);
                        intent1.putExtra("tv", movie_id);
                        startActivity(intent1);

                    }
                });


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        Person_Details.this);

                if (person_more_movie_list == null || person_more_movie_list.size() == 0) {
                    Toast.makeText(Person_Details.this, "No More Movie is Found or Network Error",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {

                    builderAlertDialog
                            .setView(view1)
                            .show();

                }


            }
        });

        popular_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Person_Details.this, "length " + popular_person_list.size(),
                        Toast.LENGTH_LONG)
                        .show();

                View view1 = getLayoutInflater().inflate(R.layout.review, null);
                ListView movieView = (ListView) view1.findViewById(R.id.review_list);

                Popular_Person_Adapter adapter = new Popular_Person_Adapter
                        (popular_person_list, Person_Details.this);

                movieView.setAdapter(adapter);

                movieView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Movie current = popular_person_list.get(position);
                        String movie_id = String.valueOf(current.getId());

                        Intent intent1 = new Intent(Person_Details.this, Person_Details.class);
                        intent1.putExtra("tv", movie_id);
                        startActivity(intent1);

                    }
                });


                AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                        Person_Details.this);

                if (popular_person_list == null || popular_person_list.size() == 0) {
                    Toast.makeText(Person_Details.this, "No Popular Person is Found or Network " +
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


        movie_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Person_Details.this, Movie_Details.class);
                intent1.putExtra("tv", mid1);
                startActivity(intent1);
            }
        });

        movie_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Person_Details.this, Movie_Details.class);
                intent1.putExtra("tv", mid2);
                startActivity(intent1);
            }
        });

        movie_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Person_Details.this, Movie_Details.class);
                intent1.putExtra("tv", mid3);
                startActivity(intent1);
            }
        });


    }

    private void initialization() {

        image_cover = (ImageView) findViewById(R.id.cover_person);
        image_cover1 = (ImageView) findViewById(R.id.coverPerson);
        image_poster = (ImageView) findViewById(R.id.postar_image_detail);
        name = (TextView) findViewById(R.id.name_person);
        biography = (TextView) findViewById(R.id.description_text);
        birth_date = (TextView) findViewById(R.id.person_birth_death);
        birth_place = (TextView) findViewById(R.id.person_birth_place);
        homepage = (TextView) findViewById(R.id.person_homepage);
        hide = (ImageView) findViewById(R.id.hide);
        show = (ImageView) findViewById(R.id.show);
        person_more_image = (TextView) findViewById(R.id.person_more_image);
        person_more_movies = (TextView) findViewById(R.id.person_more_movies);
        popular_person = (Button) findViewById(R.id.recent_popular_person);
        popular_person.setSelected(true);


        image1 = (ImageView) findViewById(R.id.person_details_image1);
        image2 = (ImageView) findViewById(R.id.person_details_image2);
        image3 = (ImageView) findViewById(R.id.person_details_image3);

        movie_image1 = (ImageView) findViewById(R.id.person_details_movie_image1);
        movie_image2 = (ImageView) findViewById(R.id.person_details_movie_image2);
        movie_image3 = (ImageView) findViewById(R.id.person_details_movie_image3);

        movie_name1 = (TextView) findViewById(R.id.person_details_movie_name1);
        movie_name2 = (TextView) findViewById(R.id.person_details_movie_name2);
        movie_name3 = (TextView) findViewById(R.id.person_details_movie_name3);


        imagesPerson = (TextView) findViewById(R.id.person_more_ima);
        moviesPerson = (TextView) findViewById(R.id.person_more_movi);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_person__details, menu);
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
}