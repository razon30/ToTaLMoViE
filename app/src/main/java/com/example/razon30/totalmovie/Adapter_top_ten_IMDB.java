package com.example.razon30.totalmovie;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by razon30 on 27-07-15.
 */
public class Adapter_top_ten_IMDB extends RecyclerView.Adapter<Adapter_top_ten_IMDB
        .ViewHolderCast>{

    Context context;
    private ArrayList<Movie> cast_list = new ArrayList<>();
    private LayoutInflater layoutInflater;
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


        if (position == 0) {
            holder.layout.setBackgroundResource(R.color.background2);
            holder.cardView.setBackgroundResource(R.color.background5);
            holder.movieThumbnail.setBackgroundResource(R.color.background6);
        } else if (position == 1) {
            holder.layout.setBackgroundResource(R.color.background4);
            holder.cardView.setBackgroundResource(R.color.background6);
            holder.movieThumbnail.setBackgroundResource(R.color.accentColor);
        } else if (position % 2 == 0) {
            holder.layout.setBackgroundResource(R.color.primaryColor);
            holder.cardView.setBackgroundResource(R.color.accentColor);
            holder.movieThumbnail.setBackgroundResource(R.color.primaryColorDark);
        } else if (position % 3 == 0) {
            holder.layout.setBackgroundResource(R.color.background5);
            holder.cardView.setBackgroundResource(R.color.primaryColorDark);
            holder.movieThumbnail.setBackgroundResource(R.color.background2);
        } else if (position % 4 == 0) {
            holder.layout.setBackgroundResource(R.color.background2);
            holder.cardView.setBackgroundResource(R.color.accentColor);
            holder.movieThumbnail.setBackgroundResource(R.color.background5);
        } else if (position % 5 == 0) {
            holder.layout.setBackgroundResource(R.color.background7);
            holder.cardView.setBackgroundResource(R.color.background6);
            holder.movieThumbnail.setBackgroundResource(R.color.accentColor);
        } else {
            holder.layout.setBackgroundResource(R.color.background2);
            holder.cardView.setBackgroundResource(R.color.background1);
            holder.movieThumbnail.setBackgroundResource(R.color.background3);
        }

        Movie current_movie = cast_list.get(position);

        String cast_thumbnile = current_movie.getImdb_url_poster();
        if (cast_thumbnile != null && cast_thumbnile != ""){

            Picasso.with(context).load(cast_thumbnile).into(holder.movieThumbnail);

        }else
        {
            holder.movieThumbnail.setImageResource(R.drawable.ic_launcher);
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

        ImageView movieThumbnail;
        TextView title;
        TextView year;
        RatingBar ratingBar;
        CardView cardView;
        RelativeLayout layout;
        public ViewHolderCast(View itemView) {
            super(itemView);

            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            title = (TextView) itemView.findViewById(R.id.movieTitle);
            year = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            ratingBar = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout_relative);

        }
    }



}
