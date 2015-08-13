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

    public void setTrailer_title(String trailer_title) {
        this.trailer_title = trailer_title;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getImdb_id_top() {
        return imdb_id_top;
    }

    public void setImdb_id_top(String imdb_id_top) {
        this.imdb_id_top = imdb_id_top;
    }

    public String getImdb_title() {
        return imdb_title;
    }

    public void setImdb_title(String imdb_title) {
        this.imdb_title = imdb_title;
    }

    public String getImdb_url_poster() {
        return imdb_url_poster;
    }

    public void setImdb_url_poster(String imdb_url_poster) {
        this.imdb_url_poster = imdb_url_poster;
    }

    public String getYear_imdb() {
        return year_imdb;
    }

    public void setYear_imdb(String year_imdb) {
        this.year_imdb = year_imdb;
    }

    public int getRating_imdb() {
        return rating_imdb;
    }

    public void setRating_imdb(int rating_imdb) {
        this.rating_imdb = rating_imdb;
    }

    public long getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(long genre_id) {
        this.genre_id = genre_id;
    }

    public String getGenre_name() {
        return genre_name;
    }

    public void setGenre_name(String genre_name) {
        this.genre_name = genre_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
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

    public void setReleaseDateTheater(String releaseDateTheater) {
        this.releaseDateTheater = releaseDateTheater;
    }

    public int getAudienceScore() {
        return audienceScore;
    }

    public void setAudienceScore(int audienceScore) {
        this.audienceScore = audienceScore;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUrlThumbnail() {
        return urlThumbnail;
    }

    public void setUrlThumbnail(String urlThumbnail) {
        this.urlThumbnail = urlThumbnail;
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

    public void setJob(String job) {
        this.job = job;
    }

    public String getProfile_thumbnail() {
        return profile_thumbnail;
    }

    public void setProfile_thumbnail(String profile_thumbnail) {
        this.profile_thumbnail = profile_thumbnail;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
