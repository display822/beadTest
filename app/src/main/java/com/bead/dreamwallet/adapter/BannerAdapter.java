package com.bead.dreamwallet.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bead.dreamwallet.entity.BannerEntity;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by DOY on 2017/7/28.
 */
public class BannerAdapter implements BGABanner.Adapter<ImageView, BannerEntity> {


    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, BannerEntity model, int position) {
        Glide.with(itemView.getContext()).load(model.getTitleImg()).into(itemView);
    }
}
