package com.bead.dreamwallet.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.AppUtil;
import com.example.skn.framework.util.DataUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.bead.dreamwallet.R;
import com.bead.dreamwallet.activity.AddForumActivity;
import com.bead.dreamwallet.activity.DetailsByForumActivity;
import com.bead.dreamwallet.activity.LoginActivity;
import com.bead.dreamwallet.adapter.BannerAdapter;
import com.bead.dreamwallet.adapter.MyBaseAdapter;
import com.bead.dreamwallet.adapter.ViewHolder;
import com.bead.dreamwallet.databinding.FragmentForumBinding;
import com.bead.dreamwallet.databinding.LayoutFindFragmentGroupLayoutBinding;
import com.bead.dreamwallet.entity.BannerEntity;
import com.bead.dreamwallet.entity.ForumEntity;
import com.bead.dreamwallet.util.StatisticsUtil;
import com.bead.dreamwallet.util.UrlService;
import com.bead.dreamwallet.util.UserInfo;
import com.bead.dreamwallet.widget.OnNoDoubleClickListener;

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
        layoutBinding.tvTip.setText("- 论坛精选 -");
        layoutBinding.llDetail.setVisibility(View.GONE);
        forumAdapter = new ForumAdapter(mActivity, data, R.layout.item_forum);
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
                        if (bannerEntities != null && bannerEntities.size() > 0)
                            layoutBinding.banner.setData(bannerEntities, null);
                        layoutBinding.banner.setAdapter(new BannerAdapter());
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }
                });
        layoutBinding.banner.setDelegate((banner, itemView, model, position)
                ->
                AppUtil.startWeb(mActivity, ((BannerEntity) model).getUrl()));
//                BaseWebViewActivity.show(mActivity, ((BannerEntity) model).getUrl(), "水珠钱包"));
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
