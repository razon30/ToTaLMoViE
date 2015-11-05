package com.example.razon30.totalmovie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by razon30 on 11-07-15.
 */
public class MyApplication extends Application {

    public static SharedPreferences preferences;
    public static Context context;
    private static DBMovies mDatabase;
    private static  MyApplication sInstance;

    public static  MyApplication getInstance(){

        return sInstance;

    }

    public static Context getAppContext(){

        return sInstance.getApplicationContext();
    }

    public synchronized static DBMovies getWritableDatabase() {
        if (mDatabase == null) {
            mDatabase = new DBMovies(getAppContext());
        }
        return mDatabase;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mDatabase = new DBMovies(this);
        context = this;
        preferences = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
    }



}
