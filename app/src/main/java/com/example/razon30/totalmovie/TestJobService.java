package com.example.razon30.totalmovie;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.ArrayList;
import java.util.Calendar;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by razon30 on 23-10-15.
 */
public class TestJobService extends JobService {

    DBMovies dbhelper;
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        this.jobParameters = jobParameters;
        dbhelper = new DBMovies(this);
        String p = MyApplication.preferences.getString("tv", "1");

        ArrayList<Movie> movies = dbhelper.searchWish();
        Calendar cal = Calendar.getInstance();

        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int mins = cal.get(Calendar.MINUTE);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String monthStr = chooseMonth(month);

        String time = setTime(hours, mins);
        String date = day + " " + monthStr + " " + year;

//        Toast.makeText(this,time+"  "+date,Toast.LENGTH_LONG).show();
//        Toast.makeText(this,movies.get(1).getAlarmDATE()+"  "+movies.get(1).getAlarmTIME(),Toast
//                .LENGTH_LONG).show();
        int c = 0;
        for (int i = 0; i < movies.size(); i++) {

            if (date.compareTo(movies.get(i).getAlarmDATE()) != 0 || time.compareTo(movies.get(i)
                    .getAlarmTIME()) != 0) {
                c++;
                if (c == movies.size()) {
                    MyApplication.preferences.edit().putString("tv", "1").commit();
                    jobFinished(jobParameters, false);
                }
            }
        }

        //  String test=MyApplication.preferences.getString("tvv", "1");

        if (movies.size() != 0) {
            for (int i = 0; i < movies.size(); i++) {
                if (date.compareTo(movies.get(i).getAlarmDATE()) == 0) {
                    // MyApplication.preferences.edit().putString("tvv", "0").commit();
                    if (time.compareTo(movies.get(i).getAlarmTIME()) == 0 && (p == "1" || p.equalsIgnoreCase("1"))) {

                        //  Toast.makeText(this,movies.get(i).getAlarmDATE()+"  "+movies.get(i)
                        //          .getAlarmTIME(),Toast.LENGTH_LONG).show();
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(4000);


                        Intent resultIntent = new Intent(this, MainActivity.class);
                        // resultIntent.putExtra("tv",movies.get(i).getMovieID());

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
                        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
                        stackBuilder.addNextIntent(resultIntent);
                        PendingIntent resultPendingIntent =
                                stackBuilder.getPendingIntent(
                                        0,
                                        PendingIntent.FLAG_UPDATE_CURRENT
                                );


                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(this)
                                        .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                                        .setContentTitle("Movie !! Movie !! Movie !!!")
                                        .addAction(android.R.drawable.alert_light_frame, "Details", resultPendingIntent)
                                        .setColor(getResources().getColor(R.color.accent_material_dark))
                                        .setContentText("You was supposed to watch " + movies.get
                                                (i).getMovieNAME());
                        int mNotificationId = 001;


                        mBuilder.setContentIntent(resultPendingIntent);

                        // Gets an instance of the NotificationManager service
                        NotificationManager mNotifyMgr =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        // Builds the notification and issues it.
                        mNotifyMgr.notify(mNotificationId, mBuilder.build());

                        MyApplication.preferences.edit().putString("tv", "0").commit();
                        jobFinished(jobParameters, false);


                    }
                }
            }
        }

        return true;
    }

    private String chooseMonth(int month) {
        if (month == 01 || month == 1) {
            return "Jan";
        } else if (month == 02 || month == 2) {
            return "Feb";
        } else if (month == 03 || month == 3) {
            return "March";
        } else if (month == 04 || month == 4) {
            return "April";
        } else if (month == 05 || month == 5) {
            return "May";
        } else if (month == 06 || month == 6) {
            return "June";
        } else if (month == 07 || month == 7) {
            return "July";
        } else if (month == 8) {
            return "Aug";
        } else if (month == 9) {
            return "Sep";
        } else if (month == 10) {
            return "Oct";
        } else if (month == 11) {
            return "Nov";
        } else if (month == 12) {
            return "Dec";
        }
        return "null";
    }

    private String setTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        return aTime;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
