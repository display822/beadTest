package com.bead.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.StringUtil;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.bead.dreamwallet.R;
import com.bead.dreamwallet.entity.LoginEntity;
import com.bead.dreamwallet.util.UrlService;
import com.bead.dreamwallet.util.UserInfo;
import com.bead.dreamwallet.widget.OnNoDoubleClickListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码
 */

public class ChangePasswordActivity extends BaseActivity {


    private EditText et_old_pw;
    private EditText et_new_pw;
    private EditText et_new_pw2;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ChangePasswordActivity.class));
    }

    @Override
    protected void initVar() {
        setContentView(R.layout.activity_change_password);
    }

    @Override
    protected void init() {
        ToolBarUtil.getInstance(this).setTitle("修改密码").build();
        et_old_pw = (EditText) findViewById(R.id.et_old_pw);
        et_new_pw = (EditText) findViewById(R.id.et_new_pw);
        et_new_pw2 = (EditText) findViewById(R.id.et_new_pw2);
        TextView btn_change_pw = (TextView) findViewById(R.id.btn_change_pw);
        TextView tv_findpwd = (TextView) findViewById(R.id.find_pwd);

        btn_change_pw.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                submit();
            }
        });
        tv_findpwd.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                FindBackPasswordActivity.startActivity(mActivity);
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void submit() {
        String oldpw = et_old_pw.getText().toString().trim();
        String newpw = et_new_pw.getText().toString().trim();
        if (TextUtils.isEmpty(oldpw) || TextUtils.isEmpty(newpw)) {
            ToastUtil.show("请输入6~16位密码！");
            return;
        }
        if (!StringUtil.checkPasswordNumOrChar(oldpw)) {
            ToastUtil.show("请输入6~16位字符的旧密码");
            return;
        }
        if (!StringUtil.checkPasswordNumOrChar(newpw)) {
            ToastUtil.show("请输入6~16位字符的新密码");
            return;
        }
        String pw2 = et_new_pw2.getText().toString().trim();
        if (!TextUtils.equals(newpw, pw2)) {
            ToastUtil.show("两次输入的新密码不相同");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("token", UserInfo.loginToken);
        map.put("oldPwd", oldpw);
        map.put("newPwd", newpw);
        Api.getDefault(UrlService.class).modifyPwd(map).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<LoginEntity>(mActivity, true) {
                    @Override
                    public void onSuccess(LoginEntity loginEntity) {
                        ToastUtil.show("密码修改成功！");
                        UserInfo.saveUserInfo(UserInfo.phone, loginEntity.getToken(), loginEntity.getPwd());
                        finish();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }
                });

    }
}
