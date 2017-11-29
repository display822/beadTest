package com.dreamwallet.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dreamwallet.R;
import com.dreamwallet.activity.LoansDetailsActivity;
import com.dreamwallet.activity.MainActivity;
import com.dreamwallet.activity.MyActivityActivity;
import com.dreamwallet.adapter.GalleryAdapter;
import com.dreamwallet.adapter.HomeInformationAdapter;
import com.dreamwallet.databinding.DefaultProductBinding;
import com.dreamwallet.databinding.FragmentHomeBinding;
import com.dreamwallet.entity.BannerEntity;
import com.dreamwallet.entity.BorrowRecordEntity;
import com.dreamwallet.entity.HomeInformationEntity;
import com.dreamwallet.entity.StarProductEntity;
import com.dreamwallet.util.StatisticsUtil;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.widget.DepthPageTransformer;
import com.dreamwallet.widget.GlideRoundBitmap;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.AppUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hf
 * 首页
 */
public class HomeFragment extends BaseFragment {


    private FragmentHomeBinding binding;
    private boolean bannerComplete;
    private boolean recordComplete;
    private boolean productComplete;
    private boolean informationComplete;
    public static final int BANNER_SCROLL = 1;
    private boolean isHidden = false;

    int currentPosition = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            //如果没有隐藏，继续发消息
            switch (msg.what){
                case BANNER_SCROLL:
                    handler.removeMessages(BANNER_SCROLL);
                    //banner滚动
                    binding.banner1.setCurrentItem(currentPosition+1);
                    if(!isHidden){
                        handler.sendEmptyMessageDelayed(BANNER_SCROLL, 2500);
                    }
                    break;
            }
        }
    };

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.refresh.setOnRefreshListener(refreshLayout -> init());
        setHasOptionsMenu(true);
        StatisticsUtil.homePage(mActivity);
        binding.refresh.autoRefresh();
        return binding.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if(!hidden){
            handler.sendEmptyMessageDelayed(BANNER_SCROLL, 1000);
        }
    }

    public void init() {
        initBanner();
        initBorrowRecord();
        initStartProduct();
        initInformation();
        initClickListener();
    }

    private void initClickListener() {

        binding.setTipClick(view -> MyActivityActivity.startActivity(mActivity));

        binding.setLimitClick(view -> {
            //额度高
            ((MainActivity) mActivity).showLoansByPosition(1);
            StatisticsUtil.visitCount(mActivity, StatisticsUtil.BorrowClick, "2");
        });
        binding.setRateClick(view -> {
            //利率低
            ((MainActivity) mActivity).showLoansByPosition(2);
            StatisticsUtil.visitCount(mActivity, StatisticsUtil.BorrowClick, "3");
        });


        binding.setInformationClick(view -> ((MainActivity) mActivity).showFindByPosition(0));
        binding.setCommunityClick(view -> ((MainActivity) mActivity).showFindByPosition(1));
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
                case 4:
                    informationComplete = true;
                    break;
            }
            if (bannerComplete && recordComplete && productComplete && informationComplete) {
                binding.refresh.finishRefresh();
                bannerComplete = false;
                recordComplete = false;
                productComplete = false;
                informationComplete = false;
            }
        }
    }

    //banner
    private void initBanner() {
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
                                Glide.with(mActivity).load(b.getTitleImg()).placeholder(R.drawable.ic_banner_holder).
                                        error(R.drawable.ic_banner_holder).bitmapTransform(new GlideRoundBitmap(mActivity, 8)).into(iv);
                                imgs.add(iv);

                                iv.setOnClickListener(view ->
                                        {
                                            AppUtil.startWeb(mActivity, b.getUrl());
                                            StatisticsUtil.visitCount(mActivity, StatisticsUtil.banner, position + "");
                                        }
                                );
                            }
                            binding.banner1.setPageMargin(25);
                            binding.banner1.setAdapter(new GalleryAdapter(imgs));
                            binding.banner1.setOffscreenPageLimit(3);
                            binding.banner1.setPageTransformer(true, new DepthPageTransformer());
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
                                    if (state != ViewPager.SCROLL_STATE_IDLE) return;
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
                                handler.sendEmptyMessageDelayed(BANNER_SCROLL, 1500);
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

            //统计
            //StatisticsUtil.visitCount(mActivity, StatisticsUtil.banner, (position + 1) + "");
            //点击事件
            //AppUtil.startWeb(mActivity, ((BannerEntity) model).getUrl());
    }

    //跑马灯效果
    private void initBorrowRecord() {
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
                            binding.text.setData(titleList, null);
                        }
                        closeRefresh(2);
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        closeRefresh(2);
                    }
                });

    }

    //明星产品
    private void initStartProduct() {
        Api.getDefault(UrlService.class).getStarProduct().compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<StarProductEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<StarProductEntity> starProductEntities) {
                        if(starProductEntities != null && starProductEntities.size() >0)
                            binding.product.removeAllViews();

                        for (StarProductEntity item : starProductEntities) {
                            LayoutInflater inflater = LayoutInflater.from(mActivity);
                            DefaultProductBinding starbinding = DataBindingUtil.inflate(inflater, R.layout.default_product, null, false);
                            starbinding.setData(item);
                            starbinding.tvInfo.setText("·"+item.getLend_speed() + "放款 ·月费率" + item.getDaily_interest_rate() + "%");
                            starbinding.setApplyClick(view -> {
                                StatisticsUtil.visitCount(mActivity, StatisticsUtil.starPlatform, item.getId());
                                LoansDetailsActivity.startActivity(mActivity, item.getId());
                            });

                            //添加到linearlayout
                            binding.product.addView(starbinding.getRoot());
                        }

                        closeRefresh(3);
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        closeRefresh(3);
                    }
                });

    }

    //资讯
    private void initInformation() {
        Api.getDefault(UrlService.class).getIndexNews().compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<HomeInformationEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<HomeInformationEntity> homeInformationEntities) {
                        if (homeInformationEntities != null && homeInformationEntities.size() > 0) {
                            binding.rvInformation.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                            binding.rvInformation.setAdapter(new HomeInformationAdapter(homeInformationEntities, mActivity));
                        }
                        closeRefresh(4);
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        closeRefresh(4);
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.text.clearViewFlipper();
    }
}
