package com.example.razon30.totalmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by razon30 on 27-07-15.
 */
public class Adapter_review_fragment extends RecyclerView.Adapter<Adapter_review_fragment
        .ViewHolderBoxOffice> {

    private ArrayList<Movie> listMovies = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;

    String image_url = "http://image.tmdb.org/t/p/w342";
    private int previousPosition = 0;

    public Adapter_review_fragment(Context context) {

        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getsInstance();

    }

    public void setMovies(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderBoxOffice onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_movie_box_office, parent, false);
        ViewHolderBoxOffice viewHolder = new ViewHolderBoxOffice(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBoxOffice holder, int position) {




    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class ViewHolderBoxOffice extends RecyclerView.ViewHolder {
        ImageView movieThumbnail;
        TextView movieTitle;
        TextView movieHeadline;

        public ViewHolderBoxOffice(View itemView) {
            super(itemView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieHeadline = (TextView) itemView.findViewById(R.id.movieReleaseDate);

        }
    }


}
