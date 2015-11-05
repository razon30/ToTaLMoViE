package com.example.razon30.totalmovie;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;


public class Trailer extends android.support.v4.app.Fragment implements UpcomingMoviesLoadedListener{


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listview;
    Adapter_trailer adapter;
    ArrayList<Movie> trailer_list = new ArrayList<Movie>();
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private String mParam1;
    private String mParam2;


    public Trailer() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Trailer newInstance(String param1, String param2) {
        Trailer fragment = new Trailer();
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
