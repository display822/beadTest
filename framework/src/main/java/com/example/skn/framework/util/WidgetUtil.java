package com.example.skn.framework.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;

/**
 * Created by hf
 */
public class WidgetUtil {
    /**
     * 是否滑到底部
     * RecyclerView
     */
    public static boolean isSlideToBottom(RecyclerView recyclerView, boolean canLoad) {
        return recyclerView != null
                && recyclerView.computeVerticalScrollExtent()//view可视高度
                + recyclerView.computeVerticalScrollOffset()//之前划过的高度
                >= recyclerView.computeVerticalScrollRange()//view总高度
                && canLoad;
    }

    /**
     * 是否滑到底部
     * ListView
     */
    public static boolean isSlideToBottom(final AbsListView listView, boolean canLoad) {
        boolean result = false;
        if (listView.getLastVisiblePosition() == (listView.getCount() - 1)) {
            final View bottomChildView = listView.getChildAt
                    (listView.getLastVisiblePosition() - listView.getFirstVisiblePosition());
            result = (listView.getHeight() >= bottomChildView.getBottom());
        }
        return result && canLoad;
    }
}
