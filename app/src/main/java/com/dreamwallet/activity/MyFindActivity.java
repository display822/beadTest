package com.dreamwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.dreamwallet.R;
import com.dreamwallet.databinding.ActivityMyFindBinding;
import com.dreamwallet.databinding.ItemMyFindBinding;
import com.dreamwallet.entity.MyFindEntity;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;
import com.dreamwallet.widget.OnNoDoubleClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hf
 * 我的发现
 */
public class MyFindActivity extends BaseActivity {

    private boolean isEdit = true;
    private List<MyFindEntity> data = new ArrayList<>();
    private MyFindAdapter myFindAdapter;
    private int pageNum = 1;
    private final int pageSize = 10;
    private int visitType = 1;
    private int REFRESH = 0;
    private int LOAD = 1;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyFindActivity.class);
        context.startActivity(intent);
    }

    private ActivityMyFindBinding binding;

    @Override
    protected void initVar() {
        setFlagTranslucentStatus();
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_my_find);
        ToolBarUtil.getInstance(mActivity).setTitle("我的发现").build();
        myFindAdapter = new MyFindAdapter(mActivity, data);
        binding.rlMyFind.setLayoutManager(new LinearLayoutManager(mActivity));
        binding.rlMyFind.setAdapter(myFindAdapter);
        binding.refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                getData(LOAD);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getData(REFRESH);
            }
        });
        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    isEdit = false;
                    binding.tvEdit.setText("取消");
                    myFindAdapter.refresh(true);
                    binding.rlDelete.setVisibility(View.VISIBLE);
                } else {
                    isEdit = true;
                    myFindAdapter.refresh(false);
                    binding.tvEdit.setText("编辑");
                    binding.rlDelete.setVisibility(View.GONE);
                }
            }
        });
        binding.tvDelete.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (myFindAdapter.getSelectorItem().size() > 0) {
                    deleteData();
                }
            }
        });
        binding.setIsInformation(true);
        binding.setInvitationClick(view -> {
            visitType = 2;
            pageNum = 1;
            binding.refresh.autoRefresh();
            binding.setIsInformation(false);
        });
        binding.setInformationClick(view -> {
            visitType = 1;
            pageNum = 1;
            binding.refresh.autoRefresh();
            binding.setIsInformation(true);
        });
        initEmptyOrNetErrorView(binding.getRoot(), R.id.viewStub, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.refresh.autoRefresh();
            }
        });
        binding.cbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.cbCheck.isChecked()) {
                    myFindAdapter.getSelectorItem().addAll(data);
                } else {
                    myFindAdapter.getSelectorItem().clear();
                }
                myFindAdapter.notifyDataSetChanged();
                binding.tvCount.setText(myFindAdapter.getSelectorItem().size() + "/" + data.size());
            }
        });

    }

    //删除帖子
    private void deleteData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", UserInfo.loginToken);
//        idStr----浏览记录id串,用逗号分割
//        visitType----访问类型 1^资讯 2^帖子
        map.put("visitType", visitType);
        String idStr = "";
        for (MyFindEntity myFindEntity : myFindAdapter.getSelectorItem()) {
            idStr = idStr.concat(myFindEntity.getVisitId() + ",");
        }
        idStr = idStr.substring(0, idStr.lastIndexOf(","));
        map.put("idStr", idStr);
        Api.getDefault(UrlService.class).deleteInformationVisit(map).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<String>(mActivity, true) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtil.show("删除成功");
                        data.removeAll(myFindAdapter.getSelectorItem());
                        myFindAdapter.refresh(!isEdit);

                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }
                });
    }

    @Override
    protected void initData() {
        binding.refresh.autoRefresh();
        binding.refresh.setDisableContentWhenRefresh(true);
    }


    private void getData(int type) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        map.put("visitType", visitType);
        map.put("token", UserInfo.loginToken);
        Api.getDefault(UrlService.class).getCustomInformationVisit(map).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<MyFindEntity>>(mActivity) {
                    @Override
                    public void onSuccess(List<MyFindEntity> myFindEntities) {
                        if (type == REFRESH) {
                            data.clear();
                        }
                        if (myFindEntities != null && myFindEntities.size() > 0) {
                            data.addAll(myFindEntities);
                        }
                        myFindAdapter.refresh(!isEdit);
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
                        myFindAdapter.refresh(!isEdit);
                        if (type == REFRESH) {
                            binding.refresh.finishRefresh();
                            updateEmptyOrNetErrorView(false, !TextUtils.equals(code, "-1"));
                        } else if (type == LOAD) {
                            binding.refresh.finishLoadmore();
                        }
                    }

                    @Override
                    public void onNotLogon() {
                        super.onNotLogon();
                        finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (32 == requestCode && resultCode == Activity.RESULT_OK) {
            getData(REFRESH);
        }
    }

    class MyFindAdapter extends RecyclerView.Adapter<MyFindAdapter.MyFindViewHolder> {
        private Context context;
        private List<MyFindEntity> data;
        private boolean isShow;
        private Set<MyFindEntity> selectorItem;

        public Set<MyFindEntity> getSelectorItem() {
            return selectorItem;
        }

        public MyFindAdapter(Context context, List<MyFindEntity> data) {
            this.context = context;
            this.data = data;
            isShow = false;
            selectorItem = new HashSet<>();
        }

        public void refresh(boolean isShow) {
            this.isShow = isShow;
            selectorItem.clear();
            binding.cbCheck.setChecked(false);
            binding.tvCount.setText(selectorItem.size() + "/" + data.size());
            notifyDataSetChanged();
        }

        @Override
        public MyFindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemMyFindBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_my_find, parent, false);
            return new MyFindViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(MyFindViewHolder holder, int position) {
            MyFindEntity myFindEntity = data.get(position);
            if (visitType == 1) {
                holder.binding.iv.setVisibility(View.VISIBLE);
                Glide.with(holder.binding.iv.getContext()).load(myFindEntity.getImg()).placeholder(R.drawable.ic_find_list_icon)
                        .error(R.drawable.ic_find_list_icon).into(holder.binding.iv);
            } else {
                holder.binding.iv.setVisibility(View.GONE);
            }
            holder.binding.setIsShow(isShow);
            holder.binding.setData(myFindEntity);
            holder.binding.cbCheck.setVisibility(isShow ? View.VISIBLE : View.GONE);
            holder.binding.cbCheck.setChecked(selectorItem.contains(myFindEntity));
            holder.binding.cbCheck.setOnClickListener(v -> {
                if (!selectorItem.contains(myFindEntity)) {
                    selectorItem.add(myFindEntity);
                } else {
                    selectorItem.remove(myFindEntity);
                }
                binding.tvCount.setText(selectorItem.size() + "/" + data.size());
                if (selectorItem.size() == data.size() && selectorItem.size() > 0) {
                    binding.cbCheck.setChecked(true);
                } else {
                    binding.cbCheck.setChecked(false);
                }
            });
            holder.binding.executePendingBindings();
            holder.binding.getRoot().setOnClickListener(v -> {
                if (!isShow) {
                    if (visitType == 1) {
                        DetailsActivity.startActivity(mActivity, myFindEntity.getInformationId());
                    } else {
                        DetailsByForumActivity.startActivity(mActivity, myFindEntity.getInformationId());
                    }
                } else {
                    if (selectorItem.contains(myFindEntity)) {
                        selectorItem.remove(myFindEntity);
                    } else {
                        selectorItem.add(myFindEntity);
                    }
                    holder.binding.cbCheck.setChecked(selectorItem.contains(myFindEntity));
                    binding.tvCount.setText(selectorItem.size() + "/" + data.size());
                    if (selectorItem.size() == data.size() && selectorItem.size() > 0) {
                        binding.cbCheck.setChecked(true);
                    } else {
                        binding.cbCheck.setChecked(false);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyFindViewHolder extends RecyclerView.ViewHolder {

            public ItemMyFindBinding binding;

            public MyFindViewHolder(ItemMyFindBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

}
