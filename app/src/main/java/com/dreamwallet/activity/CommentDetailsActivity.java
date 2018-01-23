package com.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;

import com.dreamwallet.R;
import com.dreamwallet.adapter.CommentAdapter;
import com.dreamwallet.databinding.ActivityCommentBinding;
import com.dreamwallet.entity.CommentBean;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;
import com.dreamwallet.widget.OnNoDoubleClickListener;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hf
 * 全部评论
 */
public class CommentDetailsActivity extends BaseActivity {

    private int csmId;
    private List<CommentBean> data = new ArrayList<>();
    private int pageNum = 1;
    private final int pageSize = 20;
    private int REFRESH = 0;
    private int LOAD = 1;
    private ActivityCommentBinding binding;
    private CommentAdapter commentAdapter;
    AnimationDrawable drawable;

    public static void StartActivity(Context context, int csmId) {
        Intent intent = new Intent(context, CommentDetailsActivity.class);
        intent.putExtra("csmId", csmId);
        context.startActivity(intent);
    }

    @Override
    protected void initVar() {

        setFlagTranslucentStatus();
        csmId = getIntent().getIntExtra("csmId", 0);
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_comment);

        drawable = (AnimationDrawable) binding.refreshAnim.getDrawable();
        drawable.start();
        binding.titleBack.setOnClickListener(view -> finish());
        commentAdapter = new CommentAdapter(mActivity, data);
        commentAdapter.setHideLastLine(true);
        binding.rlComment.setAdapter(commentAdapter);
        binding.refresh.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                getDate(LOAD);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getDate(REFRESH);
            }
        });
        binding.refresh.autoRefresh();
        binding.tvSend.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (UserInfo.isLogin()) {
                    saveForum();
                } else {
                    LoginActivity.startActivity(mActivity, LoginActivity.FINISH);
                }
            }
        });
        initEmptyOrNetErrorView(binding.getRoot(), R.id.viewStub, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.refresh.autoRefresh();
            }
        });
    }

    @Override
    protected void initData() {

    }

    //添加评论
    private void saveForum() {
        String content = binding.etInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) return;
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", UserInfo.loginToken);
        map.put("postId", csmId);
        map.put("content", content);
        Api.getDefault(UrlService.class).addForumResult(map,"android").compose(Api.handlerResult())
                .subscribe(new RequestCallBack<String>(mActivity, true) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtil.show("评论成功");
                        binding.etInput.setText("");
                        binding.refresh.autoRefresh();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }
                });
    }

    private void getDate(int type) {
        Api.getDefault(UrlService.class).getAllComment(csmId, pageNum, pageSize, "android").compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<CommentBean>>(mActivity) {
                    @Override
                    public void onSuccess(List<CommentBean> commentList) {
                        if (type == REFRESH) {
                            data.clear();
                        }
                        if (commentList != null && commentList.size() > 0) {
                            data.addAll(commentList);
                            binding.refresh.setEnableLoadmore(commentList.size() <= pageSize);
                        }
                        commentAdapter.notifyDataSetChanged();
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
                        commentAdapter.notifyDataSetChanged();
                        if (type == REFRESH) {
                            binding.refresh.finishRefresh();
                            updateEmptyOrNetErrorView(false, !TextUtils.equals(code, "-1"));
                        } else if (type == LOAD) binding.refresh.finishLoadmore();

                    }
                });
    }
}
