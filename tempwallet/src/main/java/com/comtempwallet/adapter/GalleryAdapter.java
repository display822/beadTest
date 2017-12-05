package com.comtempwallet.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/22 0022.
 *
 */

public class GalleryAdapter extends PagerAdapter {


    List<ImageView> datas;

    public GalleryAdapter(List<ImageView> imgs){
        datas = imgs;
    }

    @Override
    public int getCount() {
        return datas!=null ? datas.size(): 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(datas.get(position));
        return datas.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(datas.get(position));
    }

}
