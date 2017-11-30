package com.dreamwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewStubCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dreamwallet.R;
import com.dreamwallet.adapter.MyTagAdapter;
import com.dreamwallet.databinding.ActivityApplyRecordBinding;
import com.dreamwallet.databinding.ItemLoansRecordBinding;
import com.dreamwallet.entity.ApplyRecordBean;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hf
 */
public class ApplyRecordActivity extends BaseActivity {

    private int pageNum = 1;
    private int pageSize = 5;
    private int REFRASH = 0;
    private int LOAD = 1;
    private ActivityApplyRecordBinding binding;
    private ApplyRecordAdapter applyRecordAdapter;
    private List<ApplyRecordBean> data = new ArrayList<>();
    private View.OnClickListener btClickListener,freshClickListener;
    AnimationDrawable drawable;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ApplyRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVar() {
        setFlagTranslucentStatus();
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_apply_record);

        drawable = (AnimationDrawable) binding.refreshAnim.getDrawable();
        drawable.start();
        binding.titleBack.setOnClickListener(view -> finish());
        binding.rlApply.setLayoutManager(new LinearLayoutManager(mActivity));
        applyRecordAdapter = new ApplyRecordAdapter(mActivity, data);
        binding.rlApply.setAdapter(applyRecordAdapter);

        binding.refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getDate(REFRASH);
            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                getDate(LOAD);
            }
        });
        binding.refresh.autoRefresh();
        initEmptyOrNetErrorView(binding.getRoot(), R.id.viewStub,
                view -> {MainActivity.startActivity(this, 1);finish();},
                view1 -> binding.refresh.autoRefresh());
    }

    private void getDate(int refrash) {
        Api.getDefault(UrlService.class).getApplyRecord(UserInfo.loginToken, pageNum, pageSize).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<ApplyRecordBean>>(mActivity) {
                    @Override
                    public void onSuccess(List<ApplyRecordBean> list) {
                        if (refrash == REFRASH) {
                            data.clear();
                        }
                        if (list != null && list.size() > 0) {
                            data.addAll(list);
                        }
                        applyRecordAdapter.notifyDataSetChanged();
                        if (refrash == REFRASH) {
                            binding.refresh.finishRefresh();
                            updateEmptyOrNetErrorView(data.size() > 0, true);
                        } else if (refrash == LOAD) binding.refresh.finishLoadmore();

                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        if (refrash == REFRASH) {
                            data.clear();
                        }
                        applyRecordAdapter.notifyDataSetChanged();
                        if (refrash == REFRASH) {
                            binding.refresh.finishRefresh();
                            updateEmptyOrNetErrorView(false, !TextUtils.equals(code, "-1"));
                        } else if (refrash == LOAD) binding.refresh.finishLoadmore();

                    }

                    @Override
                    public void onNotLogon() {
                        super.onNotLogon();
                        finish();
                    }
                });
    }

    public void initEmptyOrNetErrorView(View rootView, int emptyOrNetErrorRes, View.OnClickListener btClickListener, View.OnClickListener freshClickListener) {
        View emptyView = rootView.findViewById(emptyOrNetErrorRes);
        if (emptyView instanceof ViewStubCompat) {
            mEmptyStub = (ViewStubCompat) emptyView;
        } else {
            mEmptyView = emptyView;
        }

        this.btClickListener = btClickListener;
        this.freshClickListener = freshClickListener;
        isInitEmptyView = true;
    }

    public void updateEmptyOrNetErrorView(boolean hasData, boolean network) {
        if (isInitEmptyView) {
            if (!hasData) {
                if (mEmptyView == null && mEmptyStub != null) {
                    mEmptyView = mEmptyStub.inflate();
                }
                if (mEmptyView != null) {
                    View ll_net_error = mEmptyView.findViewById(R.id.ll_net_error);
                    View empty = mEmptyView.findViewById(R.id.tv_empty);
                    if (!network) {
                        View viewById = mEmptyView.findViewById(com.example.skn.framework.R.id.tv_reload);
                        if (viewById != null) {
                            viewById.setOnClickListener(freshClickListener);
                        }
                        ll_net_error.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    } else {
                        Button button = mEmptyView.findViewById(R.id.tv_goloan);
                        if(button != null){
                            button.setOnClickListener(btClickListener);
                        }
                        empty.setVisibility(View.VISIBLE);
                        ll_net_error.setVisibility(View.GONE);
                    }
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            } else {
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (32 == requestCode && resultCode == Activity.RESULT_OK) {
            getDate(REFRASH);
        }
    }

    class ApplyRecordAdapter extends RecyclerView.Adapter<ApplyRecordAdapter.ApplyRecordViewHolder> {
        private Context context;
        private List<ApplyRecordBean> data;

        public ApplyRecordAdapter(Context context, List<ApplyRecordBean> data) {
            this.context = context;
            this.data = data;
        }


        @Override
        public ApplyRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemLoansRecordBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_loans_record, parent, false);
            return new ApplyRecordViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(ApplyRecordViewHolder holder, int position) {
            holder.binding.setData(data.get(position));
            holder.binding.tvInfo.setText(data.get(position).getLendSpeed() + "放款\n月费率" + data.get(position).getDailyInterestRate() + "%\n贷款期限" + data.get(position).getCycle());
            holder.binding.setApplyClick(view -> LoansDetailsActivity.startActivity(mActivity, data.get(position).getProductId() + ""));
            if (!TextUtils.isEmpty(data.get(position).getLabel())) {
                String[] labels = data.get(position).getLabel().split(",");
                holder.binding.flLabel.setVisibility(View.VISIBLE);
                holder.binding.flLabel.setAdapter(new MyTagAdapter(labels));
            } else holder.binding.flLabel.setVisibility(View.GONE);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ApplyRecordViewHolder extends RecyclerView.ViewHolder {

            public ItemLoansRecordBinding binding;

            public ApplyRecordViewHolder(ItemLoansRecordBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }


}
