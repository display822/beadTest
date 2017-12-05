package com.comtempwallet.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.comtempwallet.R;
import com.comtempwallet.adapter.GalleryAdapter;
import com.comtempwallet.databinding.FragmentFloansBinding;
import com.comtempwallet.entity.BannerEntity;
import com.comtempwallet.entity.BorrowRecordEntity;
import com.comtempwallet.http.UrlService;
import com.comtempwallet.widget.FixSpeedScroller;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2017/12/4 0004.
 *
 */

public class LoansFragment extends BaseFragment {

    private FragmentFloansBinding binding;
    int currentPosition = 0;
    private boolean isHidden = false;
    public static final int BANNER_SCROLL = 1;
    private boolean bannerComplete;
    private boolean recordComplete;
    private boolean productComplete;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            //如果没有隐藏，继续发消息
            switch (msg.what){
                case BANNER_SCROLL:
                    handler.removeMessages(BANNER_SCROLL);
                    //banner滚动
                    binding.banner1.setCurrentItem(currentPosition+1, true);
                    if(!isHidden){
                        handler.sendEmptyMessageDelayed(BANNER_SCROLL, 2500);
                    }
                    break;
            }
        }
    };

    public static LoansFragment getInstance(){
        return new LoansFragment();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if(!hidden){
            handler.sendEmptyMessageDelayed(BANNER_SCROLL, 1000);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_floans, container, false);

        binding.refresh.setOnRefreshListener(r -> init());
        binding.refresh.autoRefresh();

        return binding.getRoot();
    }

    private void closeRefresh(int type) {
        if (binding.refresh.isRefreshing()) {
            switch (type) {
                case 1:
                    bannerComplete = true;
                    break;
                case 2:
                    recordComplete = true;
                    break;
                case 3:
                    productComplete = true;
                    break;
            }
            if (bannerComplete && recordComplete && productComplete) {
                binding.refresh.finishRefresh();
                bannerComplete = false;
                recordComplete = false;
                productComplete = false;
            }
        }
    }

    /**
     * 下拉刷新后执行的方法
     */
    private void init(){

        initBanner();
        initBorrowRecord();
    }

    private void initBanner(){
        Api.getDefault(UrlService.class).getBannar().compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<BannerEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<BannerEntity> bannerEntities) {

                        if(bannerEntities != null && bannerEntities.size() > 0){
                            binding.banner1.setVisibility(View.VISIBLE);
                            binding.banner1No.setVisibility(View.GONE);
                            boolean isNolimit = false;
                            if(bannerEntities.size()>1){
                                isNolimit = true;
                                //添加无限滑动
                                bannerEntities.add(0, bannerEntities.get(bannerEntities.size()-1));
                                bannerEntities.add(bannerEntities.get(1));
                            }
                            List<ImageView> imgs = new ArrayList<>();
                            for(int i=0 ; i< bannerEntities.size(); i++){
                                BannerEntity b = bannerEntities.get(i);
                                int position = i;
                                ImageView iv = new ImageView(getActivity());
                                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                Glide.with(mActivity).load(b.getTitleImg()).placeholder(R.drawable.ic_back).
                                        error(R.drawable.ic_back).bitmapTransform(new RoundedCornersTransformation(mActivity, 8, 0)).into(iv);
                                imgs.add(iv);

                                iv.setOnClickListener(view ->
                                        {
                                            //点击事件

                                        }
                                );
                            }
                            binding.banner1.setPageMargin(25);
                            binding.banner1.setAdapter(new GalleryAdapter(imgs));
                            binding.banner1.setOffscreenPageLimit(3);

                            //滚动速度
                            try {
                                Class clazz=Class.forName("android.support.v4.view.ViewPager");
                                Field f=clazz.getDeclaredField("mScroller");
                                FixSpeedScroller fixedSpeedScroller=new FixSpeedScroller(getActivity(), new LinearOutSlowInInterpolator());
                                f.setAccessible(true);
                                f.set(binding.banner1,fixedSpeedScroller);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            binding.banner1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                }

                                @Override
                                public void onPageSelected(int position) {
                                    currentPosition = position;
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {
                                    //若viewpager滑动未停止，直接返回
                                    if (state != ViewPager.SCROLL_STATE_IDLE) {
                                        handler.removeMessages(BANNER_SCROLL);
                                        return;
                                    }else{
                                        handler.sendEmptyMessageDelayed(BANNER_SCROLL, 2500);
                                    }
                                    //若当前为第一张，设置页面为倒数第二张
                                    if (currentPosition == 0) {
                                        binding.banner1.setCurrentItem(imgs.size() - 2,false);
                                    } else if (currentPosition == imgs.size()-1) {
                                        //若当前为倒数第一张，设置页面为第二张
                                        binding.banner1.setCurrentItem(1,false);
                                    }
                                }
                            });
                            if(isNolimit){
                                handler.sendEmptyMessageDelayed(BANNER_SCROLL, 2000);
                            }

                        }else{
                            binding.banner1.setVisibility(View.GONE);
                            binding.banner1No.setVisibility(View.VISIBLE);
                        }

                        closeRefresh(1);
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        binding.banner1.setVisibility(View.GONE);
                        binding.banner1No.setVisibility(View.VISIBLE);
                        closeRefresh(1);
                    }
                });
    }

    private void initBorrowRecord(){
        Api.getDefault(UrlService.class).getBorrowRecord().compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<BorrowRecordEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<BorrowRecordEntity> borrowRecordEntities) {
                        if (borrowRecordEntities != null && borrowRecordEntities.size() > 0) {
                            ArrayList<String> titleList = new ArrayList<>();
                            for (BorrowRecordEntity item : borrowRecordEntities) {
                                if (item != null) {
                                    titleList.add("尾号"+item.getPhone()+"的用户在["+item.getProduct_name()+"]成功借款"+item.getMoney()+"元");
                                }
                            }
                            binding.borrowRecord.setData(titleList, null);
                        }
                        closeRefresh(2);
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        closeRefresh(2);
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        binding.borrowRecord.clearViewFlipper();
    }
}
