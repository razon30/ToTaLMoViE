package com.example.razon30.totalmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

/**
 * Created by razon30 on 22-07-15.
 */
public class Adapter_Genre_Recycle extends RecyclerView.Adapter<Adapter_Genre_Recycle.ViewHolderBoxOffice> {


    private ArrayList<Movie> listMovies = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    String image_url = "http://image.tmdb.org/t/p/w342";
    private int previousPosition = 0;

    public Adapter_Genre_Recycle(Context context) {

        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setMovies(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderBoxOffice onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.genre_item, parent, false);
        ViewHolderBoxOffice viewHolder = new ViewHolderBoxOffice(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBoxOffice holder, int position) {
        Movie currentMovie = listMovies.get(position);
        holder.genreTitle.setText(currentMovie.getGenre_name());

        if (position > previousPosition) {
            AnimationUtils.animate(holder, true);
        } else {
            AnimationUtils.animate(holder, false);
        }
        previousPosition = position;

    }


    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class ViewHolderBoxOffice extends RecyclerView.ViewHolder {

        TextView genreTitle;

        public ViewHolderBoxOffice(View itemView) {
            super(itemView);

            genreTitle = (TextView) itemView.findViewById(R.id.genre_name);

        }
    }


}
