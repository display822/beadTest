package com.dreamwallet.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dreamwallet.R;
import com.dreamwallet.activity.AddForumActivity;
import com.dreamwallet.activity.DetailsByForumActivity;
import com.dreamwallet.activity.LoginActivity;
import com.dreamwallet.adapter.GalleryAdapter;
import com.dreamwallet.adapter.MyBaseAdapter;
import com.dreamwallet.adapter.ViewHolder;
import com.dreamwallet.databinding.FragmentForumBinding;
import com.dreamwallet.databinding.LayoutFindFragmentGroupLayoutBinding;
import com.dreamwallet.entity.BannerEntity;
import com.dreamwallet.entity.ForumEntity;
import com.dreamwallet.util.StatisticsUtil;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;
import com.dreamwallet.widget.DepthPageTransformer;
import com.dreamwallet.widget.GlideRoundBitmap;
import com.dreamwallet.widget.OnNoDoubleClickListener;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.AppUtil;
import com.example.skn.framework.util.DataUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hf
 * 论坛
 */
public class ForumFragment extends BaseFragment {

    public static ForumFragment getInstance() {
        return new ForumFragment();
    }


    private FragmentForumBinding binding;
    private LayoutFindFragmentGroupLayoutBinding layoutBinding;
    private int pageNum = 1;
    private int REFRESH = 0;
    private int LOAD = 1;
    private ForumAdapter forumAdapter;
    private List<ForumEntity> data;
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forum, container, false);
        init();
        return binding.getRoot();

    }

    private void init() {
        data = new ArrayList<>();
        layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(mActivity), R.layout.layout_find_fragment_group_layout, null, false);

        drawable = (AnimationDrawable) binding.refreshAnim.getDrawable();
        drawable.start();
        layoutBinding.llDetail.setVisibility(View.GONE);
        forumAdapter = new ForumAdapter(mActivity, data, R.layout.item_forum);
        binding.rlForum.setDivider(null);
        binding.rlForum.addHeaderView(layoutBinding.getRoot());
        binding.rlForum.setAdapter(forumAdapter);
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
        binding.addForum.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (UserInfo.isLogin()) {
                    AddForumActivity.startActivity(mActivity);
                } else {
                    LoginActivity.startActivity(mActivity, LoginActivity.FINISH);
                }
            }
        });
    }


    private void initData(int type) {
        getBanner();
        getData(type);

    }

    private void getBanner() {
        Api.getDefault(UrlService.class).getBbsBannar().compose(Api.handlerResult())
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
                                        AppUtil.startWeb(mActivity, b.getUrl()));
                            }
                            layoutBinding.banner2.setPageMargin(25);
                            layoutBinding.banner2.setAdapter(new GalleryAdapter(imgs));
                            layoutBinding.banner2.setOffscreenPageLimit(3);

                            layoutBinding.banner2.setPageTransformer(true, new DepthPageTransformer());
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
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        layoutBinding.banner2.setVisibility(View.GONE);
                        layoutBinding.banner2No.setVisibility(View.VISIBLE);
                    }
                });
//        layoutBinding.banner.setDelegate((banner, itemView, model, position)
//                ->
//                AppUtil.startWeb(mActivity, ((BannerEntity) model).getUrl()));
//                BaseWebViewActivity.show(mActivity, ((BannerEntity) model).getUrl(), "追梦钱包"));
    }

    public void getData(int type) {
        int pageSize = 5;
        Api.getDefault(UrlService.class).getForumList(pageNum, pageSize).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<ForumEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<ForumEntity> forumEntityList) {
                        if (type == REFRESH) {
                            data.clear();
                        }
                        if (forumEntityList != null && forumEntityList.size() > 0) {
                            data.addAll(forumEntityList);
                        }
                        forumAdapter.notifyDataSetChanged();
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
                        forumAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * 适配器
     */
    class ForumAdapter extends MyBaseAdapter<ForumEntity> {

        public ForumAdapter(Context context, List<ForumEntity> list, int layoutId) {
            super(context, list, layoutId);
        }

        @Override
        public void bindData(ViewHolder viewHolder, ForumEntity item, int position) {
            viewHolder.setText(R.id.tv_time, String.valueOf(DataUtil.getTime(item.getTime())));
            viewHolder.setText(R.id.tv_mes, String.valueOf(item.getNum()));
            viewHolder.setText(R.id.tv_title, item.getTitle());
            ImageView iv = viewHolder.getView(R.id.iv);
//            Glide.with(mActivity).load(item.getImgUrl()).placeholder(R.drawable.ic_find_list_icon).error(R.drawable.ic_find_list_icon).into(iv);
            viewHolder.getView(R.id.ll_item).setOnClickListener(new OnNoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    StatisticsUtil.statistics(mActivity, StatisticsUtil.FORUM, item.getId());
                    DetailsByForumActivity.startActivity(mActivity, data.get(position).getId());
                }
            });
        }
    }

}
