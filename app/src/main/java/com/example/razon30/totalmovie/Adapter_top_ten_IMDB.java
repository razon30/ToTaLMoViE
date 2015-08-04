package com.example.razon30.totalmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by razon30 on 27-07-15.
 */
public class Adapter_top_ten_IMDB extends RecyclerView.Adapter<Adapter_top_ten_IMDB
        .ViewHolderCast>{

    private ArrayList<Movie> cast_list = new ArrayList<>();
    private LayoutInflater layoutInflater;

    Context context;
    private int previousPosition=0;


    public Adapter_top_ten_IMDB(Context context, ArrayList<Movie> cast_list) {

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.cast_list = cast_list;

    }

    @Override
    public Adapter_top_ten_IMDB.ViewHolderCast onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_movie_box_office, parent, false);
        ViewHolderCast viewHolder = new ViewHolderCast(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Adapter_top_ten_IMDB.ViewHolderCast holder, int position) {

        Movie current_movie = cast_list.get(position);

        String cast_thumbnile = current_movie.getImdb_url_poster();
        if (cast_thumbnile != null && cast_thumbnile != ""){

            Picasso.with(context).load(cast_thumbnile).into(holder.posterThumbnail);

        }else
        {
            holder.posterThumbnail.setImageResource(R.drawable.ic_launcher);
        }

        String cast_name = current_movie.getImdb_title();
        holder.title.setText(cast_name);
        String year = current_movie.getYear_imdb();
        holder.year.setText(year);

        int audienceScore = current_movie.getRating_imdb();
        if (audienceScore == -1) {
            holder.ratingBar.setRating(0.0F);
            holder.ratingBar.setAlpha(0.5F);
        } else {
            holder.ratingBar.setRating(audienceScore / 2.0F);
            holder.ratingBar.setAlpha(1.0F);
        }

        if(position>previousPosition)
        {
            AnimationUtils.animate(holder,true);
        }
        else{
            AnimationUtils.animate(holder, false);
        }
        previousPosition=position;



    }

    @Override
    public int getItemCount() {
        return cast_list.size();
    }

    public class ViewHolderCast extends RecyclerView.ViewHolder {

        ImageView posterThumbnail;
        TextView title;
        TextView year;
        RatingBar ratingBar;

        public ViewHolderCast(View itemView) {
            super(itemView);

            posterThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            title = (TextView) itemView.findViewById(R.id.movieTitle);
            year = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            ratingBar = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);

        }
    }



}
