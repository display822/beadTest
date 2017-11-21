package com.beadwallet.activity;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.beadwallet.R;
import com.beadwallet.databinding.ActivityAddForumBinding;
import com.beadwallet.util.UrlService;
import com.beadwallet.util.UserInfo;
import com.beadwallet.widget.OnNoDoubleClickListener;

import java.util.HashMap;
import java.util.Map;

public class AddForumActivity extends BaseActivity {

    private ActivityAddForumBinding binding;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddForumActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initVar() {

    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_add_forum);
        ToolBarUtil.getInstance(mActivity).setTitle("发帖").build();
        binding.tvAdd.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                addForum();
            }
        });

    }

    private void addForum() {
        String title = binding.etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            ToastUtil.show("帖子标题不能为空");
            return;
        }
        if (title.length() < 6 || title.length() > 20) {
            ToastUtil.show("标题只能输入6-20个字以内");
            return;
        }
        String content = binding.etContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.show("帖子内容不能为空");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", UserInfo.loginToken);
        map.put("title", title);
        map.put("content", content);
        Api.getDefault(UrlService.class).addForum(map).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<String>(mActivity, true) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtil.show("发帖成功");
                        finish();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }
                });
    }

    @Override
    protected void initData() {

    }


}
