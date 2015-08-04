package com.example.razon30.totalmovie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by razon30 on 11-07-15.
 */
public class MovieSorter {

    public void sortMoviesByName(ArrayList<Movie> movies){

        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });

    }

    public void sortMovieByDate(ArrayList<Movie> movies){

        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {

                if(lhs.getReleaseDateTheater()!=null && rhs.getReleaseDateTheater()!=null)
                {
                    return rhs.getReleaseDateTheater().compareTo(lhs.getReleaseDateTheater());
                }
                else {
                    return 0;
                }
            }
        });

    }
    public void sortMoviesByRating(ArrayList<Movie> movies){
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                int ratingLhs=lhs.getAudienceScore();
                int ratingRhs=rhs.getAudienceScore();
                if(ratingLhs<ratingRhs)
                {
                    return 1;
                }
                else if(ratingLhs>ratingRhs)
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
        });
    }


}
