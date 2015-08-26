package com.example.razon30.totalmovie;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by razon30 on 25-08-15.
 */
public class MyTextViewOne extends TextView {

    public MyTextViewOne(Context context) {
        super(context);
        init();
    }

    public MyTextViewOne(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewOne(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyTextViewOne(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "roboto_slab_regular.ttf");
        setTypeface(tf);
    }

}
