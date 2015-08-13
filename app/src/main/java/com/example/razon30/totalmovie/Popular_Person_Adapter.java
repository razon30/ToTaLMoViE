package com.example.razon30.totalmovie;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by razon30 on 21-07-15.
 */
public class Popular_Person_Adapter extends BaseAdapter{

    private static LayoutInflater inflater = null;
    ArrayList<Movie> arrayList;
    Context context;
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

        ImageView movieThumbnail = (ImageView) view.findViewById(R.id.popular_person_iamge);
        TextView title = (TextView) view.findViewById(R.id.popular_person_name);
        //TextView character = (TextView) view.findViewById(R.id.popular_person_known_for);


        Movie currentMovie = arrayList.get(position);

        Picasso.with(context).load(image_url + currentMovie.getProfile_thumbnail()).into(movieThumbnail);
        title.setText(currentMovie.getName());

        CardView cardView;
        RelativeLayout layout;
        cardView = (CardView) view.findViewById(R.id.card_view);
        layout = (RelativeLayout) view.findViewById(R.id.layout_relative);

        if (position == 0) {
            layout.setBackgroundResource(R.color.background2);
            cardView.setBackgroundResource(R.color.background5);
            movieThumbnail.setBackgroundResource(R.color.background6);
        } else if (position == 1) {
            layout.setBackgroundResource(R.color.background4);
            cardView.setBackgroundResource(R.color.background6);
            movieThumbnail.setBackgroundResource(R.color.accentColor);
        } else if (position % 2 == 0) {
            layout.setBackgroundResource(R.color.primaryColor);
            cardView.setBackgroundResource(R.color.accentColor);
            movieThumbnail.setBackgroundResource(R.color.primaryColorDark);
        } else if (position % 3 == 0) {
            layout.setBackgroundResource(R.color.background5);
            cardView.setBackgroundResource(R.color.primaryColorDark);
            movieThumbnail.setBackgroundResource(R.color.background2);
        } else if (position % 4 == 0) {
            layout.setBackgroundResource(R.color.background2);
            cardView.setBackgroundResource(R.color.accentColor);
            movieThumbnail.setBackgroundResource(R.color.background5);
        } else if (position % 5 == 0) {
            layout.setBackgroundResource(R.color.background7);
            cardView.setBackgroundResource(R.color.background6);
            movieThumbnail.setBackgroundResource(R.color.accentColor);
        } else {
            layout.setBackgroundResource(R.color.background2);
            cardView.setBackgroundResource(R.color.background1);
            movieThumbnail.setBackgroundResource(R.color.background3);
        }


        // character.setText(currentMovie.getJob());

        return view;
    }

}
