package com.beadwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.umeng.socialize.UMShareAPI;
import com.beadwallet.R;
import com.beadwallet.adapter.CommentAdapter;
import com.beadwallet.databinding.ActivityDetailsByForumBinding;
import com.beadwallet.entity.ForumDetailsEntity;
import com.beadwallet.util.ShareUtil;
import com.beadwallet.util.UrlService;
import com.beadwallet.util.UserInfo;
import com.beadwallet.widget.OnNoDoubleClickListener;

import java.util.HashMap;

/**
 * Created by hf
 * 帖子详情
 */
public class DetailsByForumActivity extends BaseActivity {

    private ActivityDetailsByForumBinding binding;
    private int cmsId;

    public static void startActivity(Context context, int cmsId) {
        Intent intent = new Intent(context, DetailsByForumActivity.class);
        intent.putExtra("cmsId", cmsId);
        context.startActivity(intent);
    }

    @Override
    protected void initVar() {
        cmsId = getIntent().getIntExtra("cmsId", 0);
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_details_by_forum);
        ToolBarUtil.getInstance(mActivity).setTitle("").build();
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
        binding.tvShowAllComment.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                CommentDetailsActivity.StartActivity(mActivity, cmsId);
            }
        });
    }

    @Override
    protected void initData() {
        Api.getDefault(UrlService.class).getForumDetails(cmsId).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<ForumDetailsEntity>(mActivity, true) {
                    @Override
                    public void onSuccess(ForumDetailsEntity forumDetailsEntity) {
                        if (forumDetailsEntity != null) {
                            binding.setData(forumDetailsEntity.getPostInfo());
                            if (forumDetailsEntity.getCommentList() != null && forumDetailsEntity.getCommentList().size() > 0) {
                                binding.rlForum.setAdapter(new CommentAdapter(mActivity, forumDetailsEntity.getCommentList()));
                            }
                        }
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }
                });

    }

    private void saveForum() {
        String content = binding.etInpit.getText().toString().trim();
        if (TextUtils.isEmpty(content)) return;
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", UserInfo.loginToken);
        map.put("postId", cmsId);
        map.put("content", content);
        Api.getDefault(UrlService.class).addForumResult(map).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<String>(mActivity, true) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtil.show("评论成功");
                        binding.etInpit.setText("");
                        initData();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                if (UserInfo.isLogin()) {
                    ShareUtil.ShareWeb(mActivity);
                } else {
                    LoginActivity.startActivity(mActivity, LoginActivity.FINISH);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
