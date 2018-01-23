package com.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamwallet.R;
import com.dreamwallet.databinding.ItemInformationListBinding;
import com.dreamwallet.entity.InformationListEntity;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.widget.OnNoDoubleClickListener;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hf
 */
public class InformationListActivity extends BaseActivity {

    private int informationType;
    private int pageNum = 1;
    private int pageSize = 5;
    private int REFRESH = 0;
    private int LOAD = 1;
    private InformationListAdapter informationListAdapter;
    private RecyclerView rlInformation;
    private List<InformationListEntity> data;
    private SmartRefreshLayout refresh;
    private String title;

    AnimationDrawable drawable;

    public static void startActivity(Context context, int informationType, String title) {
        Intent intent = new Intent(context, InformationListActivity.class);
        intent.putExtra("informationType", informationType);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void initVar() {
        setFlagTranslucentStatus();

        informationType = getIntent().getIntExtra("informationType", 0);
        title = getIntent().getStringExtra("title");
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_information_list);

        drawable = (AnimationDrawable) ((ImageView) findViewById(R.id.refresh_anim)).getDrawable();
        drawable.start();
        ((TextView) findViewById(R.id.title_tv)).setText(title);
        rlInformation = (RecyclerView) findViewById(R.id.rl_information);
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        rlInformation.setLayoutManager(new LinearLayoutManager(mActivity));
        data = new ArrayList<>();
        informationListAdapter = new InformationListAdapter(mActivity, data);
        rlInformation.setAdapter(informationListAdapter);
        refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                getData(LOAD);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                data.clear();
                pageNum = 1;
                getData(REFRESH);
            }
        });

        findViewById(R.id.title_back).setOnClickListener(view -> finish());

    }

    @Override
    protected void initData() {
        refresh.autoRefresh();
    }


    private void getData(int type) {
        Api.getDefault(UrlService.class)
                .getInformationBranchList(pageNum, pageSize, informationType, "android")
                .compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<InformationListEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<InformationListEntity> informationListEntities) {
                        if (informationListEntities != null) {
                            data.addAll(informationListEntities);
                            refresh.setEnableLoadmore(informationListEntities.size() <= pageSize);
                        }
                        informationListAdapter.notifyDataSetChanged();
                        if (type == REFRESH) refresh.finishRefresh();
                        else if (type == LOAD) refresh.finishLoadmore();

                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        informationListAdapter.notifyDataSetChanged();
                        if (type == REFRESH) refresh.finishRefresh();
                        else if (type == LOAD) refresh.finishLoadmore();
                    }
                });
    }

    class InformationListAdapter extends RecyclerView.Adapter<InformationListAdapter.InformationListViewHolder> {
        private Context context;
        private List<InformationListEntity> data;

        public InformationListAdapter(Context context, List<InformationListEntity> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public InformationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemInformationListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_information_list, parent, false);
            return new InformationListViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(InformationListViewHolder viewHolder, int position) {
            viewHolder.binding.setData(data.get(position));
            viewHolder.binding.executePendingBindings();
            viewHolder.binding.getRoot().setOnClickListener(new OnNoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    DetailsActivity.startActivity(mActivity, data.get(position).getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class InformationListViewHolder extends RecyclerView.ViewHolder {

            public ItemInformationListBinding binding;

            public InformationListViewHolder(ItemInformationListBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }


    }
}
