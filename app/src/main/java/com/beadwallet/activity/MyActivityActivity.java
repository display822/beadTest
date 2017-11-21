package com.beadwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.ToolBarUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.beadwallet.R;
import com.beadwallet.databinding.ItemMyActivityBinding;
import com.beadwallet.entity.MyActivityEntity;
import com.beadwallet.util.ShareListener;
import com.beadwallet.util.UrlService;
import com.beadwallet.util.UserInfo;
import com.beadwallet.widget.OnNoDoubleClickListener;

import java.util.List;

/**
 * Created by hf
 */
public class MyActivityActivity extends BaseActivity {
    private RecyclerView rlActivity;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyActivityActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_my_activity);
        ToolBarUtil.getInstance(mActivity).setTitle("活动").build();
        rlActivity = (RecyclerView) findViewById(R.id.rl_activity);
        rlActivity.setLayoutManager(new LinearLayoutManager(mActivity));
        View view = findViewById(R.id.activity_my_myactivity);
        initEmptyOrNetErrorView(view, R.id.viewStub, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        Api.getDefault(UrlService.class).getActivity().compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<MyActivityEntity>>(mActivity, true) {
                    @Override
                    public void onSuccess(List<MyActivityEntity> myActivityEntities) {
                        if (myActivityEntities != null && myActivityEntities.size() > 0) {
                            rlActivity.setAdapter(new MyActivityAdapter(mActivity, myActivityEntities));
                            updateEmptyOrNetErrorView(true, true);
                        } else {
                            updateEmptyOrNetErrorView(false, true);
                        }
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        updateEmptyOrNetErrorView(false, !TextUtils.equals(code, "-1"));
                    }
                });
    }

    /**
     * 适配器
     */
    class MyActivityAdapter extends RecyclerView.Adapter<MyActivityAdapter.MyActivityViewHolder> {
        private Context context;
        private List<MyActivityEntity> data;

        public MyActivityAdapter(Context context, List<MyActivityEntity> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public MyActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemMyActivityBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_my_activity, parent, false);
            return new MyActivityViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(MyActivityViewHolder holder, int position) {
            MyActivityEntity myActivityEntity = data.get(position);
            Glide.with(holder.binding.iv.getContext()).load(myActivityEntity.getImg())
                    .placeholder(R.drawable.ic_activity_cover)
                    .error(R.drawable.ic_activity_cover)
                    .into(holder.binding.iv);
            holder.binding.setData(myActivityEntity);
            holder.binding.setActivityDetailsClick(new OnNoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    DetailsActivity.startActivity(context, myActivityEntity.getId());
                }
            });
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DetailsActivity.startActivity(context, myActivityEntity.getId());
                }
            });
            holder.binding.executePendingBindings();

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyActivityViewHolder extends RecyclerView.ViewHolder {

            private ItemMyActivityBinding binding;

            public MyActivityViewHolder(ItemMyActivityBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

}
