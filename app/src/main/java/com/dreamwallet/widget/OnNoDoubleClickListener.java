package com.dreamwallet.widget;

import android.view.View;

/**
 * Created by DIY on 2017/9/4.
 */

public abstract class OnNoDoubleClickListener implements View.OnClickListener {

    private int mThrottleFirstTime = 1500;
    private long mLastClickTime = 0;

    public OnNoDoubleClickListener() {
    }

    public OnNoDoubleClickListener(int throttleFirstTime) {
        mThrottleFirstTime = throttleFirstTime;
    }

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastClickTime > mThrottleFirstTime) {
            mLastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public abstract void onNoDoubleClick(View v);
}
