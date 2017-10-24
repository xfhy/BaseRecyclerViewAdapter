package com.xfhy.basequickadapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author feiyang
 * create at 2017/10/19 10:37
 * description：从上往下
 */
public class SlideInTopAnimation implements BaseAnimation {

    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "translationY", -view.getMeasuredHeight(), 0)};
    }
}
