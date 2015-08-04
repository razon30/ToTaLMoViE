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
 * Created by razon30 on 11-07-15.
 */
public class AdapterBoxOffice extends RecyclerView.Adapter<AdapterBoxOffice.ViewHolderBoxOffice> {


    private ArrayList<Movie> listMovies = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    String image_url = "http://image.tmdb.org/t/p/w342";
    private int previousPosition=0;

    public AdapterBoxOffice(Context context) {

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
        View view = layoutInflater.inflate(R.layout.custom_movie_box_office, parent, false);
        ViewHolderBoxOffice viewHolder = new ViewHolderBoxOffice(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBoxOffice holder, int position) {
        Movie currentMovie = listMovies.get(position);
        holder.movieTitle.setText(currentMovie.getTitle());
        String movieReleaseDate = currentMovie.getReleaseDateTheater();
        if (movieReleaseDate != null || movieReleaseDate.length()>0) {
            holder.movieReleaseDate.setText(movieReleaseDate);
        } else {
            holder.movieReleaseDate.setText("NA");
        }

        int audienceScore = currentMovie.getAudienceScore();
        if (audienceScore == -1) {
            holder.movieAudienceScore.setRating(0.0F);
            holder.movieAudienceScore.setAlpha(0.5F);
        } else {
            holder.movieAudienceScore.setRating(audienceScore / 2.0F);
            holder.movieAudienceScore.setAlpha(1.0F);
        }


        if(position>previousPosition)
        {
            AnimationUtils.animate(holder,true);
        }
        else{
            AnimationUtils.animate(holder, false);
        }
        previousPosition=position;



        String urlThumnail = currentMovie.getUrlThumbnail();
        loadImages(urlThumnail, holder);

    }

    private void loadImages(String urlThumbnail, final ViewHolderBoxOffice holder) {
        if (!urlThumbnail.equals("NA") && urlThumbnail!=null && urlThumbnail.length()!=0) {

            urlThumbnail = image_url+urlThumbnail;

            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.movieThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                    holder.movieThumbnail.setImageResource(R.drawable.clearhistory);

                }
            });
        }else if (urlThumbnail==null){
            holder.movieThumbnail.setImageResource(R.drawable.clearhistory);
        }
    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }

    public class ViewHolderBoxOffice extends RecyclerView.ViewHolder {
        ImageView movieThumbnail;
        TextView movieTitle;
        TextView movieReleaseDate;
        RatingBar movieAudienceScore;

        public ViewHolderBoxOffice(View itemView) {
            super(itemView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            movieAudienceScore = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);
        }
    }

}
