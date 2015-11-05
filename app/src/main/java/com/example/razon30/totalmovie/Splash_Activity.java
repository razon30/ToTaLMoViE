package com.example.razon30.totalmovie;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Splash_Activity extends AppCompatActivity {

    public static final String TAG = "ImmersiveModeFragment";
    /**
     * Duration of wait *
     */
    private final int SPLASH_DISPLAY_LENGTH = 2500;
    LinearLayout layout;
    TextView tv;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_splash_);

        layout = (LinearLayout) findViewById(R.id.splash_layout);
        tv = (TextView) findViewById(R.id.splash_text);
        imageView = (ImageView) findViewById(R.id.splash_image);


        AnimationUtils.animateList(tv,true);
        AnimationUtils.animateList(imageView,true);


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                Intent intent = new Intent(Splash_Activity.this,MainActivity.class);
                startActivity(intent);


                overridePendingTransition(R.anim.right_in, R.anim.left);

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
