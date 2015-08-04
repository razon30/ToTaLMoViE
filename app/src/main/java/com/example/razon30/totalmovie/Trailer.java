package com.example.razon30.totalmovie;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class Trailer extends android.support.v4.app.Fragment implements UpcomingMoviesLoadedListener{


    ListView listview;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    Adapter_trailer adapter;
    ArrayList<Movie> trailer_list = new ArrayList<Movie>();
    String trailer = "http://www.myapifilms.com/imdb/trailers";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;


    // TODO: Rename and change types and number of parameters
    public static Trailer newInstance(String param1, String param2) {
        Trailer fragment = new Trailer();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Trailer() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_trailer, container, false);
        listview = (ListView) view.findViewById(R.id.list_trailer);

        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();

        adapter = new Adapter_trailer(getActivity());
        if (trailer_list==null || trailer_list.size()==0){
            new TrailerLoadTask(this).execute();
        }else {
            adapter.getData(trailer_list);
        }


//        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, trailer, null,
//
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//
//                        if (jsonObject == null || jsonObject.length() == 0) {
//                            Toast.makeText(getActivity(), "Problem to load", Toast.LENGTH_LONG)
//                                    .show();
//
//                        }
//
//                        try {
//
//                            JSONArray traailer_array = jsonObject.getJSONArray("trailers");
//                            for (int i=0;i<traailer_array.length();i++){
//
//                                JSONObject current_trailer = traailer_array.getJSONObject(i);
//
//                                String duration = current_trailer.getString("duration");
//                                String title = current_trailer.getString("title");
//                                String url = current_trailer.getString("videoURL");
//
//
//                                Movie movie = new Movie(title,duration,url);
//                                trailer_list.add(movie);
//
//                            }
//
//
//
//
//                        } catch (Exception e) {
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
//        requestQueue.add(request1);

//        BaseAdapter adapter = new BaseAdapter() {
//            @Override
//            public int getCount() {
//                return trailer_list.size();
//            }
//
//            @Override
//            public Object getItem(int position) {
//                return trailer_list.get(position);
//            }
//
//            @Override
//            public long getItemId(int position) {
//                return position;
//            }
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//
//                View view1 = getActivity().getLayoutInflater().inflate(R.layout.trailer_item,
//                        parent,false);
//
//                TextView tvTitle = (TextView) view1.findViewById(R.id.trailer_title);
//                TextView tvDuration = (TextView) view1.findViewById(R.id.trailer_duration);
//
//                Movie movie = trailer_list.get(position);
//
//                tvTitle.setText(movie.getTrailer_title());
//                tvDuration.setText(movie.getDuration());
//
//
//                return view1;
//            }
//        };

        listview.setAdapter(adapter);


        return view;
    }

    @Override
    public void onUpcomingMoviesLoaded(ArrayList<Movie> listMovies) {

        adapter.getData(listMovies);
        trailer_list=listMovies;
        Toast.makeText(getActivity(),listMovies.toString()+"",Toast.LENGTH_LONG).show();

    }
}
