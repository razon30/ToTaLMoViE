package com.example.razon30.totalmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by razon30 on 21-07-15.
 */
public class Adapter_KKeyword_Search extends RecyclerView.Adapter<Adapter_KKeyword_Search
        .ViewHolderBoxOffice> {

    private ArrayList<Movie> listMovies = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    String image_url = "http://image.tmdb.org/t/p/w342";
    private int previousPosition = 0;
    Context context;


    public Adapter_KKeyword_Search(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getsInstance();
    }

    public void setMovies(ArrayList<Movie> listMovies) {
        this.listMovies = listMovies;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderBoxOffice onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_cast_and_crew, parent, false);
        ViewHolderBoxOffice viewHolder = new ViewHolderBoxOffice(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBoxOffice holder, int position) {
        Movie currentMovie = listMovies.get(position);
        holder.movieTitle.setText(currentMovie.getName());
        String movieReleaseDate = currentMovie.getType();
        if (movieReleaseDate != null || movieReleaseDate.length() > 0) {
            holder.movieReleaseDate.setText(movieReleaseDate);
        } else {
            holder.movieReleaseDate.setText("NA");
        }



        if (position > previousPosition) {
            AnimationUtils.animate(holder, true);
        } else {
            AnimationUtils.animate(holder, false);
        }
        previousPosition = position;


        String urlThumnail = currentMovie.getProfile_thumbnail();

        Picasso.with(context).load(image_url+urlThumnail).into(holder.movieThumbnail);

    }


    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class ViewHolderBoxOffice extends RecyclerView.ViewHolder {
        ImageView movieThumbnail;
        TextView movieTitle;
        TextView movieReleaseDate;


        public ViewHolderBoxOffice(View itemView) {
            super(itemView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.cast_thumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.name_cast);
            movieReleaseDate = (TextView) itemView.findViewById(R.id.cast_job);
        }
    }


}
