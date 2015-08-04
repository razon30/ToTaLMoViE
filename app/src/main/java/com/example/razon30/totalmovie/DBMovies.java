package com.example.razon30.totalmovie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by razon30 on 11-07-15.
 */
public class DBMovies extends SQLiteOpenHelper {

    SQLiteDatabase db;
    public static final String DATABASE_NAME = "Database_Movie_";
    public static final int DB_VERSION = 1;

    public static final String DB_TABLE_NAME_BOX_OFFICE = "Box_Office";
    public static final String DB_TABLE_NAME_UPCOMING = "Box_Office_upcoming";
    public static final String DB_TABLE_NAME_SEARCHING = "Box_Office_searching";
    public static final String ID_FIELD = "_id";
    public static final String Movie_Id = "movie_id";
    public static final String TITLE = "title";
    public static final String RATINGS = "ratings";
    public static final String RELEASE_DATE = "release_date";
    public static final String SYNOPSIS = "synopsis";
    public static final String URLTHUMBNILE = "urlThubnile";

    // CREATING THE TABLE
    public static final String MOVIES_TABLE_SQL_BOX_OFFICE = "CREATE TABLE "
            + DB_TABLE_NAME_BOX_OFFICE + " ( " + ID_FIELD
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + Movie_Id + " TEXT, " + TITLE
            + " TEXT, " + RATINGS + " TEXT, " + RELEASE_DATE + " TEXT, " + SYNOPSIS
            + " TEXT, " + URLTHUMBNILE + " TEXT)";

    public static final String MOVIES_TABLE_SQL_UPCOMING = "CREATE TABLE "
            + DB_TABLE_NAME_UPCOMING + " ( " + ID_FIELD
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + Movie_Id + " TEXT, " + TITLE
            + " TEXT, " + RATINGS + " TEXT, " + RELEASE_DATE + " TEXT, " + SYNOPSIS
            + " TEXT, " + URLTHUMBNILE + " TEXT)";

    public static final String MOVIES_TABLE_SQL_SEARCHING = "CREATE TABLE "
            + DB_TABLE_NAME_SEARCHING + " ( " + ID_FIELD
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + Movie_Id + " TEXT, " + TITLE
            + " TEXT, " + RATINGS + " TEXT, " + RELEASE_DATE + " TEXT, " + SYNOPSIS
            + " TEXT, " + URLTHUMBNILE + " TEXT)";


    Context context;
    public DBMovies(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(MOVIES_TABLE_SQL_BOX_OFFICE);
        Log.e("TABLE CREAT", MOVIES_TABLE_SQL_BOX_OFFICE);

        db.execSQL(MOVIES_TABLE_SQL_UPCOMING);
        Log.e("TABLE CREAT", MOVIES_TABLE_SQL_UPCOMING);

        db.execSQL(MOVIES_TABLE_SQL_SEARCHING);
        Log.e("TABLE CREAT", MOVIES_TABLE_SQL_SEARCHING);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void insertdata_boxOffice (ArrayList<Movie> listMovies, boolean clearall){

        SQLiteDatabase db = this.getWritableDatabase();

        if (clearall){
            deleateAll_BOX_OFFICE();
        }


        //create a sql prepared statement
        String sql = "INSERT INTO " + DB_TABLE_NAME_BOX_OFFICE + " VALUES (?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (int i = 0; i < listMovies.size(); i++) {
            Movie currentMovie = listMovies.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index

            statement.bindString(2, String.valueOf(currentMovie.getId()));
            statement.bindString(3, currentMovie.getTitle());
            statement.bindLong(4, currentMovie.getAudienceScore());
            statement.bindString(5, currentMovie.getReleaseDateTheater());
            statement.bindString(6, currentMovie.getSynopsis());
            statement.bindString(7, currentMovie.getUrlThumbnail());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        // L.m("inserting entries " + listMovies.size() + new Date(System.currentTimeMillis()));
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();


    }

    public void insertdata_Upcoming (ArrayList<Movie> listMovies, boolean clearall){

        SQLiteDatabase db = this.getWritableDatabase();

        if (clearall){
            deleateAll_UPCOMING();
        }


        //create a sql prepared statement
        String sql = "INSERT INTO " + DB_TABLE_NAME_UPCOMING + " VALUES (?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (int i = 0; i < listMovies.size(); i++) {
            Movie currentMovie = listMovies.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index

            statement.bindString(2, String.valueOf(currentMovie.getId()));
            statement.bindString(3, currentMovie.getTitle());
            statement.bindLong(4, currentMovie.getAudienceScore());
            statement.bindString(5, currentMovie.getReleaseDateTheater());
            statement.bindString(6, currentMovie.getSynopsis());
            statement.bindString(7, currentMovie.getUrlThumbnail());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        // L.m("inserting entries " + listMovies.size() + new Date(System.currentTimeMillis()));
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();


    }

    public void insertdata_Searching (ArrayList<Movie> listMovies, boolean clearall){

        SQLiteDatabase db = this.getWritableDatabase();

        if (clearall){
            deleateAll_Searching();
        }


        //create a sql prepared statement
        String sql = "INSERT INTO " + DB_TABLE_NAME_SEARCHING + " VALUES (?,?,?,?,?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (int i = 0; i < listMovies.size(); i++) {
            Movie currentMovie = listMovies.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index

            statement.bindString(2, String.valueOf(currentMovie.getId()));
            statement.bindString(3, currentMovie.getTitle());
            statement.bindLong(4, currentMovie.getAudienceScore());
            statement.bindString(5, currentMovie.getReleaseDateTheater());
            statement.bindString(6, currentMovie.getSynopsis());
            statement.bindString(7, currentMovie.getUrlThumbnail());

            statement.execute();
        }
        //set the transaction as successful and end the transaction
        // L.m("inserting entries " + listMovies.size() + new Date(System.currentTimeMillis()));
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();


    }


    public ArrayList<Movie> getAllMoviesBoxOffice() {
        ArrayList<Movie> listMovies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ID_FIELD,
                Movie_Id,
                TITLE,
                RATINGS,
                RELEASE_DATE,
                SYNOPSIS,
                URLTHUMBNILE
        };

        Cursor cursor = db.query(DB_TABLE_NAME_BOX_OFFICE, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                Long id = Long.valueOf(cursor.getString(cursor.getColumnIndex(Movie_Id)));
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                int rating = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RATINGS)));
                String release = cursor.getString(cursor.getColumnIndex(RELEASE_DATE));
                String synopsis = cursor.getString(cursor.getColumnIndex(SYNOPSIS));
                String url = cursor.getString(cursor.getColumnIndex(URLTHUMBNILE));

                Movie movie = new Movie(id,title,release,rating,synopsis,url);

                listMovies.add(movie);
            }
            while (cursor.moveToNext());
        }

        db.close();

        return listMovies;
    }

    public ArrayList<Movie> getAllMoviesUpcoming() {
        ArrayList<Movie> listMovies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ID_FIELD,
                Movie_Id,
                TITLE,
                RATINGS,
                RELEASE_DATE,
                SYNOPSIS,
                URLTHUMBNILE
        };

        Cursor cursor = db.query(DB_TABLE_NAME_UPCOMING, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                Long id = Long.valueOf(cursor.getString(cursor.getColumnIndex(Movie_Id)));
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                int rating = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RATINGS)));
                String release = cursor.getString(cursor.getColumnIndex(RELEASE_DATE));
                String synopsis = cursor.getString(cursor.getColumnIndex(SYNOPSIS));
                String url = cursor.getString(cursor.getColumnIndex(URLTHUMBNILE));

                Movie movie = new Movie(id,title,release,rating,synopsis,url);

                listMovies.add(movie);
            }
            while (cursor.moveToNext());
        }

        db.close();

        return listMovies;
    }

    public ArrayList<Movie> getAllMoviesSearching() {
        ArrayList<Movie> listMovies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {ID_FIELD,
                Movie_Id,
                TITLE,
                RATINGS,
                RELEASE_DATE,
                SYNOPSIS,
                URLTHUMBNILE
        };

        Cursor cursor = db.query(DB_TABLE_NAME_SEARCHING, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // L.m("loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                Long id = Long.valueOf(cursor.getString(cursor.getColumnIndex(Movie_Id)));
                String title = cursor.getString(cursor.getColumnIndex(TITLE));
                int rating = Integer.parseInt(cursor.getString(cursor.getColumnIndex(RATINGS)));
                String release = cursor.getString(cursor.getColumnIndex(RELEASE_DATE));
                String synopsis = cursor.getString(cursor.getColumnIndex(SYNOPSIS));
                String url = cursor.getString(cursor.getColumnIndex(URLTHUMBNILE));

                Movie movie = new Movie(id,title,release,rating,synopsis,url);

                listMovies.add(movie);
            }
            while (cursor.moveToNext());
        }

        db.close();

        return listMovies;
    }


    public void deleateAll_BOX_OFFICE() {

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(DB_TABLE_NAME_BOX_OFFICE, null, null);

    }

    public void deleateAll_UPCOMING() {

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(DB_TABLE_NAME_UPCOMING, null, null);

    }

    public void deleateAll_Searching() {

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(DB_TABLE_NAME_SEARCHING, null, null);

    }


}
