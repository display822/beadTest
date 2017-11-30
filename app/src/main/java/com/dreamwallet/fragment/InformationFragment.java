package com.dreamwallet.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
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
import com.dreamwallet.R;
import com.dreamwallet.activity.DetailsActivity;
import com.dreamwallet.activity.InformationListActivity;
import com.dreamwallet.adapter.GalleryAdapter;
import com.dreamwallet.adapter.MyBaseAdapter;
import com.dreamwallet.adapter.ViewHolder;
import com.dreamwallet.databinding.FragmentInformationBinding;
import com.dreamwallet.databinding.LayoutFindFragmentGroupLayoutBinding;
import com.dreamwallet.entity.BannerEntity;
import com.dreamwallet.entity.InformationEntity;
import com.dreamwallet.util.StatisticsUtil;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.widget.DepthPageTransformer;
import com.dreamwallet.widget.FixSpeedScroller;
import com.dreamwallet.widget.GlideRoundBitmap;
import com.dreamwallet.widget.OnNoDoubleClickListener;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.AppUtil;
import com.example.skn.framework.util.DataUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hf
 * 资讯界面
 */
public class InformationFragment extends BaseFragment {

    public static InformationFragment getInstance() {
        return new InformationFragment();
    }


    private FragmentInformationBinding binding;
    private LayoutFindFragmentGroupLayoutBinding layoutBinding;
    private int pageNum = 1;
    private final int pageSize = 5;
    private int REFRESH = 0;
    private int LOAD = 1;
    private InformationAdapter informationAdapter1;
    private List<InformationEntity> data;
    int currentPosition = 0;
    AnimationDrawable drawable;

    public static final int BANNER_SCROLL = 1;
    private boolean isHidden = false;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            //如果没有隐藏，继续发消息
            switch (msg.what){
                case BANNER_SCROLL:
                    handler.removeMessages(BANNER_SCROLL);
                    //banner滚动
                    layoutBinding.banner2.setCurrentItem(currentPosition+1, true);
                    if(!isHidden){
                        handler.sendEmptyMessageDelayed(BANNER_SCROLL, 2500);
                    }
                    break;
            }
        }
    };

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_information, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        data = new ArrayList<>();
        layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.layout_find_fragment_group_layout, null, false);
        informationAdapter1 = new InformationAdapter(mActivity, data, R.layout.item_information);

        drawable = (AnimationDrawable) binding.refreshAnim.getDrawable();
        drawable.start();
        binding.rlInformation.setDivider(null);
        binding.rlInformation.addHeaderView(layoutBinding.getRoot());
        binding.rlInformation.setAdapter(informationAdapter1);
        binding.refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                initData(LOAD);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                initData(REFRESH);
            }
        });
        binding.refresh.autoRefresh();
        binding.refresh.setDisableContentWhenRefresh(true);
        layoutBinding.setClick1(v -> InformationListActivity.startActivity(mActivity, 1, "行业资讯"));
        layoutBinding.setClick2(v -> InformationListActivity.startActivity(mActivity, 2, "贷款知识"));
        layoutBinding.setClick3(v -> InformationListActivity.startActivity(mActivity, 3, "用卡常识"));
        layoutBinding.setClick4(v -> InformationListActivity.startActivity(mActivity, 4, "防骗课堂"));
    }


    private void initData(int type) {
        getBanner();
        getData(type);

    }

    private void getBanner() {
        Api.getDefault(UrlService.class).getInformationBannar().compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<BannerEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<BannerEntity> bannerEntities) {
                        if (bannerEntities != null && bannerEntities.size() > 0){
                            layoutBinding.banner2.setVisibility(View.VISIBLE);
                            layoutBinding.banner2No.setVisibility(View.GONE);

                            boolean isNolimit = false;
                            if(bannerEntities.size()>1){
                                isNolimit = true;
                                //添加无限滑动
                                bannerEntities.add(0, bannerEntities.get(bannerEntities.size()-1));
                                bannerEntities.add(bannerEntities.get(1));
                            }
                            List<ImageView> imgs = new ArrayList<>();
                            for(BannerEntity b : bannerEntities){
                                ImageView iv = new ImageView(getActivity());
                                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                //圆角图片
                                Glide.with(mActivity).load(b.getTitleImg()).placeholder(R.drawable.ic_banner_holder).
                                        error(R.drawable.ic_banner_holder).bitmapTransform(new GlideRoundBitmap(mActivity, 8)).into(iv);
                                imgs.add(iv);
                                iv.setOnClickListener(view ->
                                        AppUtil.startWeb(mActivity, b.getUrl())
                                );
                            }
                            layoutBinding.banner2.setPageMargin(25);
                            layoutBinding.banner2.setAdapter(new GalleryAdapter(imgs));
                            layoutBinding.banner2.setOffscreenPageLimit(3);
                            layoutBinding.banner2.setPageTransformer(true, new DepthPageTransformer());
                            //滚动速度
                            try {
                                Class clazz=Class.forName("android.support.v4.view.ViewPager");
                                Field f=clazz.getDeclaredField("mScroller");
                                FixSpeedScroller fixedSpeedScroller=new FixSpeedScroller(getActivity(), new LinearOutSlowInInterpolator());
                                f.setAccessible(true);
                                f.set(layoutBinding.banner2, fixedSpeedScroller);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            layoutBinding.banner2.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                                        layoutBinding.banner2.setCurrentItem(imgs.size() - 2,false);
                                    } else if (currentPosition == imgs.size()-1) {
                                        //若当前为倒数第一张，设置页面为第二张
                                        layoutBinding.banner2.setCurrentItem(1,false);
                                    }
                                }
                            });

                            if(isNolimit){
                                handler.sendEmptyMessageDelayed(BANNER_SCROLL, 2000);
                            }

                        }else{
                            layoutBinding.banner2.setVisibility(View.GONE);
                            layoutBinding.banner2No.setVisibility(View.VISIBLE);
                        }

//                        layoutBinding.banner.setData(bannerEntities, null);
//                        layoutBinding.banner.setAdapter(new BannerAdapter());
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        layoutBinding.banner2.setVisibility(View.GONE);
                        layoutBinding.banner2No.setVisibility(View.VISIBLE);
                    }
                });
//        layoutBinding.banner.setDelegate((banner, itemView, model, position)
//                        ->
//                        AppUtil.startWeb(mActivity, ((BannerEntity) model).getUrl())
//                BaseWebViewActivity.show(mActivity, ((BannerEntity) model).getUrl(), "追梦宝")
    }

    private void getData(int type) {
        Api.getDefault(UrlService.class).getInformationList(pageNum, pageSize).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<InformationEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<InformationEntity> informationEntities) {
                        if (type == REFRESH) {
                            data.clear();
                        }
                        if (informationEntities != null) {
                            data.addAll(informationEntities);
                        }
                        informationAdapter1.notifyDataSetChanged();
                        if (type == REFRESH) binding.refresh.finishRefresh();
                        else if (type == LOAD) binding.refresh.finishLoadmore();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        if (type == REFRESH) {
                            data.clear();
                        }
                        if (type == REFRESH) binding.refresh.finishRefresh();
                        else if (type == LOAD) binding.refresh.finishLoadmore();
                        informationAdapter1.notifyDataSetChanged();
                    }
                });
    }

    class InformationAdapter extends MyBaseAdapter<InformationEntity> {

        public InformationAdapter(Context context, List<InformationEntity> list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        public void bindData(ViewHolder viewHolder, InformationEntity item, int position) {
            viewHolder.setText(R.id.tv_time, DataUtil.getTime(item.getCreate_time()));
            viewHolder.setText(R.id.tv_title, item.getTitle());
            viewHolder.setText(R.id.tv_num, item.getCms_content_pv());
            ImageView iv = viewHolder.getView(R.id.iv);
            Glide.with(mActivity).load(item.getImg()).placeholder(R.drawable.ic_home_infoprefer).error(R.drawable.ic_home_infoprefer).into(iv);
            viewHolder.getView(R.id.ll_item).setOnClickListener(new OnNoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    StatisticsUtil.statistics(mActivity, StatisticsUtil.INFORMATION, item.getId());
                    DetailsActivity.startActivity(mActivity, item.getId());
                }
            });

        }
    }
}
