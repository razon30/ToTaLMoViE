package com.example.razon30.totalmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by razon30 on 22-07-15.
 */
public class Adapter_genre extends BaseAdapter{

    private static LayoutInflater inflater = null;
    String[] genreName = null;
    Context context;
    String image_url = "http://image.tmdb.org/t/p/w500";

    private int previousPosition=0;

    public Adapter_genre(Context context, String[] genreName) {
        this.context = context;
        this.genreName = genreName;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return genreName.length;
    }

    @Override
    public Object getItem(int position) {
        return genreName[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = inflater.inflate(R.layout.genre_item, null);

        TextView textView = (TextView) view.findViewById(R.id.genre_name);

        textView.setText(genreName[position]);

        AnimationUtils.animateToolbar(view);

        return view;
    }


}
