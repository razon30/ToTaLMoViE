package com.example.razon30.totalmovie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by razon30 on 19-07-15.
 */
public class Review_Adapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    ArrayList<Movie> arrayList;
    Context context;

    public Review_Adapter(ArrayList<Movie> arrayList, Context context) {
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

        view = inflater.inflate(R.layout.review_item, null);

        TextView textView = (TextView) view.findViewById(R.id.text_rvw_item);
        TextView author = (TextView) view.findViewById(R.id.author_rvw_item);
        author.setText(arrayList.get(position).getAuthor());
        textView.setText(arrayList.get(position).getText());


        return view;
    }
}
