package com.example.razon30.totalmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by razon30 on 18-07-15.
 */
public class Adapter_more_image extends BaseAdapter {

    private static LayoutInflater inflater = null;
    ArrayList<String> arrayList;
    Context context;
    String image_url = "http://image.tmdb.org/t/p/w500";

    public Adapter_more_image(ArrayList<String> arrayList, Context context) {
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

        view = inflater.inflate(R.layout.more_image_list_image, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.more_image_list_item);

        Picasso.with(context).load(image_url+arrayList.get(position)).into(imageView);

        return view;
    }
}
