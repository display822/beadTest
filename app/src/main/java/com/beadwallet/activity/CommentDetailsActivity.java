package com.beadwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.beadwallet.R;
import com.beadwallet.adapter.CommentAdapter;
import com.beadwallet.databinding.ActivityCommentBinding;
import com.beadwallet.entity.CommentBean;
import com.beadwallet.entity.ForumDetailsEntity;
import com.beadwallet.util.UrlService;
import com.beadwallet.util.UserInfo;
import com.beadwallet.widget.OnNoDoubleClickListener;

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

    public static void StartActivity(Context context, int csmId) {
        Intent intent = new Intent(context, CommentDetailsActivity.class);
        intent.putExtra("csmId", csmId);
        context.startActivity(intent);
    }

    @Override
    protected void initVar() {
        csmId = getIntent().getIntExtra("csmId", 0);
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_comment);
        ToolBarUtil.getInstance(mActivity).setTitle("全部评论").build();
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
        Api.getDefault(UrlService.class).addForumResult(map).compose(Api.handlerResult())
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
        Api.getDefault(UrlService.class).getAllComment(csmId, pageNum, pageSize).compose(Api.handlerResult())
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
