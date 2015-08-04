package com.example.razon30.totalmovie;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Splash_Activity extends AppCompatActivity {

    /**
     * Duration of wait *
     */
    private final int SPLASH_DISPLAY_LENGTH = 2500;
    LinearLayout layout;
    TextView tv;
    ImageView imageView;

    public static final String TAG = "ImmersiveModeFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);


//        if (Build.VERSION.SDK_INT >= 21) {
//            TransitionInflater inflater = TransitionInflater.from(this);
//            Transition transition = inflater.inflateTransition(R.transition.content_transition_a);
//            getWindow().setExitTransition(transition);
//            Slide slide = new Slide();
//            slide.setDuration(5000);
//            getWindow().setReenterTransition(slide);
//        }
//
        setContentView(R.layout.activity_splash_);

//        final View decorView = Splash_Activity.this.getWindow().getDecorView();
//        decorView.setOnSystemUiVisibilityChangeListener(
//                new View.OnSystemUiVisibilityChangeListener() {
//                    @Override
//                    public void onSystemUiVisibilityChange(int i) {
//                        int height = decorView.getHeight();
//                        Log.i(TAG, "Current height: " + height);
//                    }
//                });


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
//                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation
//                        (Splash_Activity.this, null);
//                startActivity(new Intent(Splash_Activity.this, MainActivity.class), compat.toBundle
//                        ());

                Intent intent = new Intent(Splash_Activity.this,MainActivity.class);
                startActivity(intent);

                //overridePendingTransition(R.anim.rotate_out,R.anim.rotate_in);

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
