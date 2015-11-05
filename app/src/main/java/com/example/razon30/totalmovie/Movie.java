package com.example.razon30.totalmovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by razon30 on 11-07-15.
 */
public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    public static final Parcelable.Creator<Movie> creator = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }

    };
    //for trailer
    String duration, trailer_title, videoURL;
    //for watch wish
    String w_id, w_name;
    //top 10 imdb
    String imdb_id_top,imdb_title,imdb_url_poster,year_imdb;
    int rating_imdb;
    //movie details
    String backdrop_path;
    String genre;
    String overview;
    String poster_path;
    String revenue;
    String tagLine;
    String imdb_id;
    String homepage;
    String production;
    //movie cast and crew
    String name;
    String job;
    String profile_thumbnail;
    //reviews
    String author;
    String text;
    String type;
    //for genre and id
    long genre_id;
    String genre_name;
    StringBuilder stringBuilder;
    //for watch and wish
    String movieID, movieNAME, alarmTIME, alarmDATE;
    //primary details
    private long id;
    private String title;
    private String releaseDateTheater;
    private int audienceScore;
    private String synopsis;
    private String urlThumbnail;

    public Movie(String trailer_title, String duration, String videoURL) {
        this.trailer_title = trailer_title;
        this.duration = duration;
        this.videoURL = videoURL;
    }

    public Movie(String imdb_id_top, String imdb_title, String imdb_url_poster, int rating_imdb,
                 String year_imdb) {
        this.imdb_id_top = imdb_id_top;
        this.imdb_title = imdb_title;
        this.imdb_url_poster = imdb_url_poster;
        this.rating_imdb = rating_imdb;
        this.year_imdb = year_imdb;
    }

    public Movie(long id, String title, String releaseDateTheater, int audienceScore, String synopsis, String urlThumbnail) {
        this.id = id;
        this.title = title;
        this.releaseDateTheater = releaseDateTheater;
        this.audienceScore = audienceScore;
        this.synopsis = synopsis;
        this.urlThumbnail = urlThumbnail;
    }
    public Movie(long genre_id, String genre_name) {
        this.genre_id = genre_id;
        this.genre_name = genre_name;
    }
    public Movie(String name, StringBuilder stringBuilder, String profile_thumbnail,long id) {
        this.name = name;
        this.stringBuilder = stringBuilder;
        this.profile_thumbnail = profile_thumbnail;
        this.id = id;
    }
    //for more movie person
    public Movie(String title, String releaseDateTheater, String urlThumbnail, String job, long
            id) {
        this.title = title;
        this.releaseDateTheater = releaseDateTheater;
        this.urlThumbnail = urlThumbnail;
        this.job = job;
        this.id = id;
    }
    public Movie(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public Movie(String movieID, String movieNAME, String alarmTIME, String alarmDATE) {
        this.movieID = movieID;
        this.movieNAME = movieNAME;
        this.alarmTIME = alarmTIME;
        this.alarmDATE = alarmDATE;
    }

    //search result for movie
    public Movie(String type,long id, String title, String releaseDateTheater, int audienceScore,
                 String urlThumbnail) {
        this.type = type;
        this.id = id;
        this.title = title;
        this.releaseDateTheater = releaseDateTheater;
        this.audienceScore = audienceScore;
        this.urlThumbnail = urlThumbnail;
    }
    //search result of cast
    public Movie(String name, String type, String profile_thumbnail,long id) {
        this.name = name;
        this.type = type;
        this.profile_thumbnail = profile_thumbnail;
        this.id = id;
    }

    //for popular person
    public Movie(String type, String profile_thumbnail,long id,String name) {
        this.name = name;
        this.type = type;
        this.profile_thumbnail = profile_thumbnail;
        this.id = id;
    }
    //search for multi
    public Movie(String profile_thumbnail,long id,String name, String type) {
        this.name = name;
        this.type = type;
        this.profile_thumbnail = profile_thumbnail;
        this.id = id;
    }
    public Movie(String title, String releaseDateTheater, int audienceScore, String
            backdrop_path, String genre, String overview, String poster_path, String revenue,
                 String tagLine, String imdb_id,String homepage,String production) {
        this.title = title;
        this.releaseDateTheater = releaseDateTheater;
        this.audienceScore = audienceScore;
        this.backdrop_path = backdrop_path;
        this.genre = genre;
        this.overview = overview;
        this.poster_path = poster_path;
        this.revenue = revenue;
        this.tagLine = tagLine;
        this.imdb_id = imdb_id;
        this.homepage = homepage;
        this.production = production;
    }

    public Movie() {
    }
    public Movie(Parcel in) {

        genre_id = in.readLong();
        genre_name = in.readString();

        id = in.readLong();
        title = in.readString();
        audienceScore = in.readInt();
        synopsis = in.readString();
        urlThumbnail = in.readString();

    }

    public static Creator<Movie> getCreator() {
        return creator;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTrailer_title() {
        return trailer_title;
    }


    public String getVideoURL() {
        return videoURL;
    }


    public String getImdb_id_top() {
        return imdb_id_top;
    }


    public String getImdb_title() {
        return imdb_title;
    }



    public String getImdb_url_poster() {
        return imdb_url_poster;
    }


    public String getYear_imdb() {
        return year_imdb;
    }


    public int getRating_imdb() {
        return rating_imdb;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }





    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDateTheater() {
        return releaseDateTheater;
    }


    public int getAudienceScore() {
        return audienceScore;
    }


    public String getSynopsis() {
        return synopsis;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }


    public String getProfile_thumbnail() {
        return profile_thumbnail;
    }


    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getMovieNAME() {
        return movieNAME;
    }

    public void setMovieNAME(String movieNAME) {
        this.movieNAME = movieNAME;
    }

    public String getAlarmTIME() {
        return alarmTIME;
    }

    public void setAlarmTIME(String alarmTIME) {
        this.alarmTIME = alarmTIME;
    }

    public String getAlarmDATE() {
        return alarmDATE;
    }

    public void setAlarmDATE(String alarmDATE) {
        this.alarmDATE = alarmDATE;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(genre_name);
        dest.writeLong(genre_id);

        dest.writeLong(id);
        dest.writeInt(audienceScore);
        dest.writeString(title);
        dest.writeString(releaseDateTheater);
        dest.writeString(synopsis);
        dest.writeString(urlThumbnail);
    }

}
