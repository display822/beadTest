package com.beadwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.dialog.DialogUtil;
import com.example.skn.framework.util.StringUtil;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.beadwallet.R;
import com.beadwallet.util.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_pw;
    private EditText et_pw2;
    private TextView btn_save_pw;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SetPasswordActivity.class));
    }

    @Override
    protected void initVar() {
        setContentView(R.layout.activity_set_password);
    }

    @Override
    protected void init() {
        ToolBarUtil.getInstance(this).setTitle("设置登录密码").build();
        et_pw = (EditText) findViewById(R.id.et_pw);
        et_pw2 = (EditText) findViewById(R.id.et_pw2);
        btn_save_pw = (TextView) findViewById(R.id.btn_save_pw);

        btn_save_pw.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }


    private void submit() {
        String password = et_pw.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.show("密码不能为空！");
            return;
        }
        String password2 = et_pw2.getText().toString().trim();
        if (TextUtils.isEmpty(password2)) {
            ToastUtil.show("再次输入的密码不能为空！");
            return;
        }
        if (!StringUtil.checkPasswordNumOrChar(password)) {
            ToastUtil.show("请输入6~16位字符的密码");
            return;
        }
        if (!StringUtil.checkPasswordNumOrChar(password2)) {
            ToastUtil.show("请输入6~16位字符的密码");
            return;
        }
        if (!TextUtils.equals(password, password2)) {
            ToastUtil.show("两次输入的密码不相同！");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("loginToken", UserInfo.loginToken);
        map.put("phone", UserInfo.phone);
        map.put("oldPassword", password);
        map.put("password", password2);
        DialogUtil.showLoading(this);
//        Api.getDefault(UrlService.class).updatePwd(map).compose(Api.handlerResult())
//                .subscribe(new RequestCallBack<String>(mActivity) {
//                    @Override
//                    public void onSuccess(String s) {
//                        ToastUtil.show(s);
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailure(String code, String errorMsg) {
//                    }
//                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_pw:
                submit();
                break;
        }
    }
}
