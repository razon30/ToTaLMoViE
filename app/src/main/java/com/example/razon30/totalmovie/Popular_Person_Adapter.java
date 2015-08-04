package com.example.razon30.totalmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by razon30 on 21-07-15.
 */
public class Popular_Person_Adapter extends BaseAdapter{

    ArrayList<Movie> arrayList;
    Context context;
    private static LayoutInflater inflater=null;
    String image_url = "http://image.tmdb.org/t/p/w500";

    public Popular_Person_Adapter(ArrayList<Movie> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.popular_person_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.popular_person_iamge);
        TextView title = (TextView) view.findViewById(R.id.popular_person_name);
        TextView character = (TextView) view.findViewById(R.id.popular_person_known_for);


        Movie currentMovie = arrayList.get(position);

        Picasso.with(context).load(image_url+currentMovie.getProfile_thumbnail()).into(imageView);
        title.setText(currentMovie.getName());
        character.setText(currentMovie.getJob());

        return view;
    }

}
