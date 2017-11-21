package com.beadwallet.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.beadwallet.entity.BannerEntity;

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
