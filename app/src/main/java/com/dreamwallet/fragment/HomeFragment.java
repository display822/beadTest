package com.dreamwallet.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dreamwallet.R;
import com.dreamwallet.activity.DetailsActivity;
import com.dreamwallet.activity.LoansDetailsActivity;
import com.dreamwallet.activity.MainActivity;
import com.dreamwallet.activity.MyActivityActivity;
import com.dreamwallet.adapter.GalleryAdapter;
import com.dreamwallet.adapter.HomeInformationAdapter;
import com.dreamwallet.adapter.HomeRecordAdapter;
import com.dreamwallet.databinding.DefaultProductBinding;
import com.dreamwallet.databinding.FragmentHomeBinding;
import com.dreamwallet.entity.BannerEntity;
import com.dreamwallet.entity.BorrowRecordEntity;
import com.dreamwallet.entity.HomeInformationEntity;
import com.dreamwallet.entity.MoneyRecord;
import com.dreamwallet.entity.StarProductEntity;
import com.dreamwallet.util.Global;
import com.dreamwallet.util.RecordDao;
import com.dreamwallet.util.StatisticsUtil;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.widget.DepthPageTransformer;
import com.dreamwallet.widget.FixSpeedScroller;
import com.dreamwallet.widget.GlideRoundBitmap;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.BaseEntity;
import com.example.skn.framework.http.RequestCallBack;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Observable;

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
                    binding.banner1.setCurrentItem(currentPosition+1, true);
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

        if(Global.hideLoans == 0){
            binding.homeNeedHide.setVisibility(View.GONE);
            binding.product.setVisibility(View.GONE);
        }else{
            binding.recordHide.setVisibility(View.GONE);
        }
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
        if(Global.hideLoans == 0){
            initMoneyRecord();
        }
        initBanner();
        initBorrowRecord();
        initStartProduct();
        initInformation();
        initClickListener();
    }

    private void initMoneyRecord(){
        RecordDao dao = new RecordDao(getActivity());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //获取当前月第一天：
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        String first = format.format(c.getTime());
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = format.format(ca.getTime());
        List<MoneyRecord> moneyRecords = dao.selectRecordByDate(first, last);
        if (moneyRecords.size() == 0){
            binding.rvMoneyrecord.setVisibility(View.GONE);
            binding.blankRecordShow.setVisibility(View.VISIBLE);
        }else{
            binding.rvMoneyrecord.setVisibility(View.VISIBLE);
            binding.blankRecordShow.setVisibility(View.GONE);
            binding.rvMoneyrecord.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            binding.rvMoneyrecord.setAdapter(new HomeRecordAdapter(moneyRecords, mActivity));
        }

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

        binding.setRecordClick(v -> ((MainActivity) mActivity).showRecordActivity());
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
        Observable<BaseEntity<List<BannerEntity>>> bannar;
        if(Global.hideLoans == 0){
            bannar = Api.getDefault(UrlService.class).getBannar("0");
        }else{
            bannar = Api.getDefault(UrlService.class).getBannar();
        }

        bannar.compose(Api.handlerResult())
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
                                            String url = b.getUrl();
                                            int indexD = url.indexOf("=");
                                            int indexY = url.indexOf("&");
                                            String platformId = indexY != -1?  url.substring(indexD+1,indexY): url.substring(indexD+1);
                                            if(Global.hideLoans == 0){
                                                DetailsActivity.startActivity(getActivity(), Integer.valueOf(platformId));
                                            }else{
                                                LoansDetailsActivity.startActivity(getActivity(), platformId);
                                            }
                                            StatisticsUtil.visitCount(mActivity, StatisticsUtil.banner, position + "");
                                        }
                                );
                            }
                            binding.banner1.setPageMargin(25);
                            binding.banner1.setAdapter(new GalleryAdapter(imgs));
                            binding.banner1.setOffscreenPageLimit(3);
                            binding.banner1.setPageTransformer(true, new DepthPageTransformer());
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

            //统计
            //StatisticsUtil.visitCount(mActivity, StatisticsUtil.banner, (position + 1) + "");
            //点击事件
            //AppUtil.startWeb(mActivity, ((BannerEntity) model).getUrl());
    }

    //跑马灯效果
    private void initBorrowRecord() {
        if(Global.hideLoans == 0) {
            closeRefresh(2);
            return;
        }

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
        if(Global.hideLoans == 0) {
            closeRefresh(3);
            return;
        }
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
