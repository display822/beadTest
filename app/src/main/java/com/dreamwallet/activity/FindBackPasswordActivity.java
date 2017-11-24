package com.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.*;
import com.dreamwallet.R;
import com.dreamwallet.entity.LoginEntity;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;
import com.dreamwallet.widget.TimeButton;

import java.util.HashMap;
import java.util.Map;

public class FindBackPasswordActivity extends BaseActivity implements View.OnClickListener
        , TimeButton.OnLoadDataListener {

    private EditText et_phone;
    private EditText et_code;
    private TimeButton tb_verify_code;
    private EditText et_password;
    private TextView btn_complete;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FindBackPasswordActivity.class));
    }


    @Override
    protected void initVar() {
        setContentView(R.layout.activity_find_back_password);
    }

    @Override
    protected void init() {
        ToolBarUtil.getInstance(this).setTitle("找回密码").build();
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        tb_verify_code = (TimeButton) findViewById(R.id.tb_verify_code);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_complete = (TextView) findViewById(R.id.btn_complete);

        tb_verify_code.setOnLoadDataListener(this);
        btn_complete.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_complete:
                submit();
                break;
        }
    }

    private void submit() {
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("手机号不能为空！");
            return;
        }
        if (!StringUtil.isMobile(phone)) {
            ToastUtil.show("手机号码有误，请你重新输入！");
            return;
        }
        String code = et_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show("验证码不能为空！");
            return;
        }
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.show("密码不能为空!");
            return;
        }
        if (!StringUtil.checkPasswordNumOrChar(password)) {
            ToastUtil.show("请输入6~16位字符的新密码");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("pwd", password);
        map.put("code", code);
        Api.getDefault(UrlService.class).resetPwd(map).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<LoginEntity>(mActivity, true) {
                    @Override
                    public void onSuccess(LoginEntity loginEntity) {
                        if (loginEntity != null) {
                            UserInfo.saveUserInfo(phone, loginEntity.getToken(), loginEntity.getPwd());
                            MainActivity.startActivity(mActivity, 0);
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                    }
                });
    }

    @Override
    public void load(final TimeButton timeButton) {
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("手机号不能为空！");
            return;
        }
        if (!StringUtil.isMobile(phone)) {
            ToastUtil.show("手机号码有误，请你重新输入！");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.clear();
        map.put("phone", StringUtil.encrypting(phone));
        map.put("smsType", 2);
        Api.getDefault(UrlService.class).sendSmsEncryption(map).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<String>(mActivity, true) {
                    @Override
                    public void onSuccess(String s) {
                        timeButton.time();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                    }
                });

    }
}
