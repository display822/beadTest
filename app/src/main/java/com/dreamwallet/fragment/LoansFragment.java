package com.dreamwallet.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.skn.framework.base.BaseFragment;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.dreamwallet.R;
import com.dreamwallet.activity.LoansDetailsActivity;
import com.dreamwallet.adapter.MyTagAdapter;
import com.dreamwallet.databinding.FragmentLoansBinding;
import com.dreamwallet.databinding.ItemLoansBinding;
import com.dreamwallet.entity.LoansEntity;
import com.dreamwallet.util.StatisticsUtil;
import com.dreamwallet.util.UrlService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hf
 * 贷款页
 */
public class LoansFragment extends BaseFragment {

    public static LoansFragment getInstance() {
        return new LoansFragment();
    }

    public int sortInfoId;
    private FragmentLoansBinding binding;
    private List<LoansEntity> data;
    private LoansAdapter adapter;
    private int pageNum = 1;
    private int REFRESH = 0;
    private int LOAD = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_loans, container, false);
        init();
        initEmptyOrNetErrorView(binding.getRoot(), R.id.viewStub, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.refresh.autoRefresh();
            }
        });
        return binding.getRoot();
    }


    public void init() {
        initRefresh();
        initRecommendProduct();
    }

    private void initRefresh() {
        binding.rlProduct.setLayoutManager(new LinearLayoutManager(mActivity));
        binding.refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                initPlatformListData(LOAD);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                initPlatformListData(REFRESH);
            }
        });
        data = new ArrayList<>();
        adapter = new LoansAdapter(mActivity, data);
        binding.rlProduct.setAdapter(adapter);
        binding.refresh.autoRefresh();
    }

    private void initRecommendProduct() {
        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();
        spring.setSpringConfig(new SpringConfig(80, 0));
        spring.setCurrentValue(20);
        spring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                binding.ivRecommendProduct.setTranslationX((float) spring.getCurrentValue());
            }
        });
        spring.setEndValue(0.1);
        binding.ivRecommendProduct.setOnClickListener(view -> {
                    LoansDetailsActivity.startActivity(mActivity, "");
                }
        );
    }

    private void initPlatformListData(int type) {
        int pageSize = 5;
        Api.getDefault(UrlService.class).getPlatformList(pageNum, pageSize, sortInfoId).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<LoansEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<LoansEntity> entities) {
                        if (type == REFRESH) {
                            data.clear();
                        }
                        if (entities != null && entities.size() > 0) {
                            data.addAll(entities);
                        }
                        adapter.notifyDataSetChanged();
                        if (type == REFRESH) {
                            binding.refresh.finishRefresh();
                            updateEmptyOrNetErrorView(data.size() > 0, true);
                        } else if (type == LOAD) binding.refresh.finishLoadmore();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        if (type == REFRESH) {
                            data.clear();
                        }
                        adapter.notifyDataSetChanged();
                        if (type == REFRESH) {
                            binding.refresh.finishRefresh();
                            updateEmptyOrNetErrorView(false, !TextUtils.equals(code, "-1"));
                        } else if (type == LOAD) binding.refresh.finishLoadmore();

                    }
                });

    }

    /**
     * 适配器
     */
    public class LoansAdapter extends RecyclerView.Adapter<LoansAdapter.StapleViewHolder> {
        private List<LoansEntity> data;
        private Context context;

        public LoansAdapter(Context context, List<LoansEntity> data) {
            this.context = context;
            this.data = data;
        }


        @Override
        public LoansAdapter.StapleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemLoansBinding itemHomeBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_loans, parent, false);
            return new StapleViewHolder(itemHomeBinding);
        }


        @Override
        public void onBindViewHolder(LoansAdapter.StapleViewHolder viewHolder, final int position) {
            LoansEntity loansEntity = data.get(position);
            viewHolder.bind.setData(loansEntity);
            viewHolder.bind.tvInfo.setText(loansEntity.getLendSpeed() + "放款 | 参考费率：" + loansEntity.getDailyInterestRate() + "% | 贷款期限" + loansEntity.getMaxCycle());
            if (!TextUtils.isEmpty(data.get(position).getLabel())) {
                String[] labels = data.get(position).getLabel().split(",");
                viewHolder.bind.flLabel.setVisibility(View.VISIBLE);
                viewHolder.bind.flLabel.setAdapter(new MyTagAdapter(labels));
            } else viewHolder.bind.flLabel.setVisibility(View.GONE);
            viewHolder.bind.setApplyClick(view -> {
                        StatisticsUtil.visitCount(mActivity, StatisticsUtil.platform, loansEntity.getPlatformId());
                        LoansDetailsActivity.startActivity(context, loansEntity.getPlatformId());
                    }

            );
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        /**
         * recycleViewHolder
         */
        class StapleViewHolder extends RecyclerView.ViewHolder {
            ItemLoansBinding bind;

            public View getRoot() {
                return itemView;
            }

            StapleViewHolder(ItemLoansBinding bind) {
                super(bind.getRoot());
                this.bind = bind;
            }
        }
    }
}
