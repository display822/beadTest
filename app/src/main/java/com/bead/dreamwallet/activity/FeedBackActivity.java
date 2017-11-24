package com.bead.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.StringUtil;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.bead.dreamwallet.R;
import com.bead.dreamwallet.util.UrlService;
import com.bead.dreamwallet.util.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 我要反馈界面
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    private RadioButton rb_apply_borrow;
    private RadioButton rb_experience;
    private RadioButton rb_other;
    private RadioGroup rg_question;
    private EditText et_phone;
    private EditText et_content;
    private TextView btn_submit;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FeedBackActivity.class));
    }

    @Override
    protected void initVar() {
        setContentView(R.layout.activity_feed_back);
    }

    @Override
    protected void init() {
        ToolBarUtil.getInstance(this).setTitle("我要反馈").build();
        rb_apply_borrow = (RadioButton) findViewById(R.id.rb_apply_borrow);
        rb_experience = (RadioButton) findViewById(R.id.rb_experience);
        rb_other = (RadioButton) findViewById(R.id.rb_other);
        rg_question = (RadioGroup) findViewById(R.id.rg_question);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_content = (EditText) findViewById(R.id.et_content);
        btn_submit = (TextView) findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(this);
        rb_apply_borrow.setChecked(true);
    }

    @Override
    protected void initData() {

    }

    private void submit() {
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请输入手机号码");
            return;
        }
        if (!StringUtil.isMobile(phone)) {
            ToastUtil.show("请输入正确的手机号码");
            return;
        }

        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请选择问题类型，帮助我们更快的处理您的反馈", Toast.LENGTH_SHORT).show();
            return;
        }
        int type = rb_apply_borrow.isChecked() ? 1 : (rb_experience.isChecked() ? 2 : 3);
        Map<String, Object> map = new HashMap<>();
        map.clear();
        map.put("token", UserInfo.loginToken);
        map.put("type", type);
        map.put("content", content);
        map.put("reservePhone", phone);
        Api.getDefault(UrlService.class).saveCustomerFeedBack(map).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<String>(mActivity,true) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtil.show("您的反馈我们已收到，我们将尽快处理！");
                        finish();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                    }
                });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                submit();
                break;
        }
    }
}
