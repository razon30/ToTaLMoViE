package com.example.razon30.totalmovie;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by razon30 on 11-07-15.
 */
public class AnimationUtils {

    public static void animate(RecyclerView.ViewHolder holder, boolean goesDown) {

        YoYo.with(Techniques.RubberBand)
                .duration(1000)
                .playOn(holder.itemView);

    }

    public static void animateList(View view, boolean goesDown) {

        YoYo.with(Techniques.RollIn)
                .duration(1500)
                .playOn(view);

    }

    public static void animateToolbar(View containerToolbar) {


        containerToolbar.setRotationX(-90);
        containerToolbar.setAlpha(0.2F);
        containerToolbar.setPivotX(0.0F);
        containerToolbar.setPivotY(0.0F);

        Animator alpha = ObjectAnimator.ofFloat(containerToolbar, "alpha", 0.2F, 0.4F, 0.6F, 0.8F, 1.0F)
                .setDuration(4000);
        Animator rotationX = ObjectAnimator.ofFloat(containerToolbar, "rotationX", -90, 60, -45,
                45, -10, 30, 0, 20, 0, 5, 0).setDuration(4000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(alpha, rotationX);
        animatorSet.start();


    }


}
