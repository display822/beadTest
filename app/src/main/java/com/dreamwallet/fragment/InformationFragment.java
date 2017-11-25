package com.dreamwallet.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.AppUtil;
import com.example.skn.framework.util.DataUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.dreamwallet.R;
import com.dreamwallet.activity.DetailsActivity;
import com.dreamwallet.activity.InformationListActivity;
import com.dreamwallet.adapter.BannerAdapter;
import com.dreamwallet.adapter.MyBaseAdapter;
import com.dreamwallet.adapter.ViewHolder;
import com.dreamwallet.databinding.FragmentInformationBinding;
import com.dreamwallet.databinding.LayoutFindFragmentGroupLayoutBinding;
import com.dreamwallet.entity.BannerEntity;
import com.dreamwallet.entity.InformationEntity;
import com.dreamwallet.util.StatisticsUtil;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.widget.OnNoDoubleClickListener;

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
        layoutBinding.tvTip.setText("- 资讯精选 -");
        informationAdapter1 = new InformationAdapter(mActivity, data, R.layout.item_information);
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
                        AppUtil.startWeb(mActivity, ((BannerEntity) model).getUrl())
//                BaseWebViewActivity.show(mActivity, ((BannerEntity) model).getUrl(), "追梦钱包")
        );
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
            ImageView iv = viewHolder.getView(R.id.iv);
            Glide.with(mActivity).load(item.getImg()).placeholder(R.drawable.ic_find_list_icon).error(R.drawable.ic_find_list_icon).into(iv);
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
