package com.beadwallet.fragment;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.beadwallet.adapter.GalleryAdapter;
import com.beadwallet.widget.DepthPageTransformer;
import com.bumptech.glide.Glide;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.AppUtil;
import com.beadwallet.R;
import com.beadwallet.activity.LoansDetailsActivity;
import com.beadwallet.activity.MainActivity;
import com.beadwallet.activity.MyActivityActivity;
import com.beadwallet.adapter.BannerAdapter;
import com.beadwallet.adapter.HomeInformationAdapter;
import com.beadwallet.databinding.DefaultProductBinding;
import com.beadwallet.databinding.FragmentHomeBinding;
import com.beadwallet.entity.BannerEntity;
import com.beadwallet.entity.BorrowRecordEntity;
import com.beadwallet.entity.HomeInformationEntity;
import com.beadwallet.entity.StarProductEntity;
import com.beadwallet.util.StatisticsUtil;
import com.beadwallet.util.UrlService;
import com.example.skn.framework.util.ToolBarUtil;
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
        ((MainActivity) mActivity).toolbar.setVisibility(View.VISIBLE);
        ToolBarUtil.getInstance(mActivity)
                .setTitle(mActivity.getString(R.string.app_name))
                .isShow(false)
                .build();
        binding.refresh.autoRefresh();
        return binding.getRoot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ((MainActivity) mActivity).toolbar.setVisibility(View.VISIBLE);
            ToolBarUtil.getInstance(mActivity)
                    .setTitle(mActivity.getString(R.string.app_name))
                    .isShow(false)
                    .build();
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
        binding.setBorrowClick(view -> {
            //综合排序
            ((MainActivity) mActivity).showLoansByPosition(0);
            StatisticsUtil.visitCount(mActivity, StatisticsUtil.BorrowClick, "1");
        });
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
        binding.setHotClick(view -> {
            //申请多
            ((MainActivity) mActivity).showLoansByPosition(3);
            StatisticsUtil.visitCount(mActivity, StatisticsUtil.BorrowClick, "4");
        });
        binding.setPeriodClick(view -> {
            //周期长
            ((MainActivity) mActivity).showLoansByPosition(4);
            StatisticsUtil.visitCount(mActivity, StatisticsUtil.BorrowClick, "5");
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
                            List<ImageView> imgs = new ArrayList<>();
                            for(BannerEntity b : bannerEntities){
                                ImageView iv = new ImageView(getActivity());
                                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                Glide.with(mActivity).load(b.getTitleImg()).into(iv);
                                imgs.add(iv);
                            }

                            binding.banner1.setPageMargin(25);
                            binding.banner1.setOffscreenPageLimit(2);
                            binding.banner1.setAdapter(new GalleryAdapter(imgs));
                            binding.banner1.setPageTransformer(true, new DepthPageTransformer());

                        }

                        closeRefresh(1);
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
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
                                    titleList.add(item.getNote());
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
                            starbinding.tvInfo.setText(item.getLend_speed() + "放款 ·月费率" + item.getDaily_interest_rate() + "%");
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!menu.hasVisibleItems()) {
            inflater.inflate(R.menu.menu_main, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info:
                MyActivityActivity.startActivity(mActivity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.text.clearViewFlipper();
    }
}
