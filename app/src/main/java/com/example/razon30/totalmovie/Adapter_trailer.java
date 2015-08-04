package com.example.razon30.totalmovie;

import android.content.Context;
import android.mtp.MtpObjectInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by razon30 on 27-07-15.
 */
public class Adapter_trailer extends BaseAdapter{

    ArrayList<Movie> trailer_list = new ArrayList<Movie>();
    private LayoutInflater layoutInflater;
    Context context;

    public Adapter_trailer(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void getData(ArrayList<Movie> trailer_list) {
        this.trailer_list = trailer_list;
    }

    @Override
    public int getCount() {
        return trailer_list.size();
    }

    @Override
    public Object getItem(int position) {
        return trailer_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view1 = layoutInflater.inflate(R.layout.trailer_item,
                parent,false);

        TextView tvTitle = (TextView) view1.findViewById(R.id.trailer_title);
        TextView tvDuration = (TextView) view1.findViewById(R.id.trailer_duration);

        Movie movie = trailer_list.get(position);

        tvTitle.setText(movie.getTrailer_title());
        tvDuration.setText(movie.getDuration());


        return view1;
    }
}
