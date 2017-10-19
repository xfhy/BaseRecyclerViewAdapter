package com.xfhy.basequickadapter.animation;

import android.animation.Animator;
import android.view.View;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public interface BaseAnimation {
    /**
     * 返回一个Animator数组,方便扩展,可以在view上加多个动画
     * @param view
     * @return
     */
    Animator[] getAnimators(View view);
}
