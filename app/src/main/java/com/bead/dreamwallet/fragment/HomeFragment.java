package com.bead.dreamwallet.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bead.dreamwallet.R;
import com.bead.dreamwallet.adapter.GalleryAdapter;
import com.bead.dreamwallet.widget.DepthPageTransformer;
import com.bead.dreamwallet.widget.GlideRoundBitmap;
import com.bumptech.glide.Glide;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.bead.dreamwallet.activity.LoansDetailsActivity;
import com.bead.dreamwallet.activity.MainActivity;
import com.bead.dreamwallet.activity.MyActivityActivity;
import com.bead.dreamwallet.adapter.HomeInformationAdapter;
import com.bead.dreamwallet.databinding.DefaultProductBinding;
import com.bead.dreamwallet.databinding.FragmentHomeBinding;
import com.bead.dreamwallet.entity.BannerEntity;
import com.bead.dreamwallet.entity.BorrowRecordEntity;
import com.bead.dreamwallet.entity.HomeInformationEntity;
import com.bead.dreamwallet.entity.StarProductEntity;
import com.bead.dreamwallet.util.StatisticsUtil;
import com.bead.dreamwallet.util.UrlService;
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

    int currentPosition = 0;

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
                            if(bannerEntities.size()>1){
                                //添加无限滑动
                                bannerEntities.add(0, bannerEntities.get(bannerEntities.size()-1));
                                bannerEntities.add(bannerEntities.get(1));
                            }
                            List<ImageView> imgs = new ArrayList<>();
                            for(BannerEntity b : bannerEntities){
                                ImageView iv = new ImageView(getActivity());
                                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                Glide.with(mActivity).load(b.getTitleImg()).
                                        bitmapTransform(new GlideRoundBitmap(mActivity, 8)).into(iv);
                                imgs.add(iv);
                            }
                            binding.banner1.setPageMargin(25);
                            binding.banner1.setAdapter(new GalleryAdapter(imgs));
                            binding.banner1.setOffscreenPageLimit(3);
                            binding.banner1.setCurrentItem(1);
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