package com.dreamwallet.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/11/30 0030.
 */

public class FixSpeedScroller extends Scroller {

    public int mDuration=1600;

    public FixSpeedScroller(Context context) {
        super(context);
    }

    public FixSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override public void startScroll(int startX, int startY, int dx, int dy) {
        startScroll(startX,startY,dx,dy,mDuration);
    }

    @Override public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        //管你 ViewPager 传来什么时间，我完全不鸟你
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public int getmDuration() {
        return mDuration;
    }

    public void setmDuration(int duration) {
        mDuration = duration;
    }

}
