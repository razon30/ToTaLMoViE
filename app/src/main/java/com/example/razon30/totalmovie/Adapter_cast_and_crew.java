package com.example.razon30.totalmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by razon30 on 15-07-15.
 */
public class Adapter_cast_and_crew  extends RecyclerView.Adapter<Adapter_cast_and_crew
        .ViewHolderCast> {

    private ArrayList<Movie> cast_list = new ArrayList<>();
    private LayoutInflater layoutInflater;
    String image_url = "http://image.tmdb.org/t/p/w342";
    Context context;
    private int previousPosition=0;


    public Adapter_cast_and_crew(Context context, ArrayList<Movie> cast_list) {

        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.cast_list = cast_list;

    }

    @Override
    public Adapter_cast_and_crew.ViewHolderCast onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_cast_and_crew, parent, false);
        ViewHolderCast viewHolder = new ViewHolderCast(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Adapter_cast_and_crew.ViewHolderCast holder, int position) {

        Movie current_movie = cast_list.get(position);

        String cast_thumbnile = current_movie.getProfile_thumbnail();
        if (cast_thumbnile != null && cast_thumbnile != ""){

            Picasso.with(context).load(image_url+cast_thumbnile).into(holder.castThumbnail);

        }else
        {
            holder.castThumbnail.setImageResource(R.drawable.ic_launcher);
        }

        String cast_name = current_movie.getName();
        holder.cast_name.setText(cast_name);
        String job = current_movie.getJob();
        holder.cast_job.setText(job);

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

        ImageView castThumbnail;
        TextView cast_name;
        TextView cast_job;

        public ViewHolderCast(View itemView) {
            super(itemView);

            castThumbnail = (ImageView) itemView.findViewById(R.id.cast_thumbnail);
            cast_name = (TextView) itemView.findViewById(R.id.name_cast);
            cast_job = (TextView) itemView.findViewById(R.id.cast_job);

        }
    }
}
