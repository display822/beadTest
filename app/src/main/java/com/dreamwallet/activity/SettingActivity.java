package com.dreamwallet.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamwallet.R;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.base.BaseWebViewActivity;
import com.example.skn.framework.dialog.DialogUtil;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;


/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void initVar() {
        setFlagTranslucentStatus();
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void init() {
        ToolBarUtil.getInstance(this).setTitle("设置").build();

        LinearLayout ll_change_pwd = (LinearLayout) findViewById(R.id.ll_change_pwd);
        LinearLayout ll_about_us = (LinearLayout) findViewById(R.id.ll_about_us);
        TextView btn_logout = (TextView) findViewById(R.id.btn_logout);

        ll_change_pwd.setOnClickListener(this);
        ll_about_us.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_change_pwd://修改密码
                ChangePasswordActivity.startActivity(this);
                break;
            case R.id.ll_about_us://关于我们
                BaseWebViewActivity.show(mActivity, "file:///android_asset/about.html", "关于我们", true);
                break;
            case R.id.btn_logout:
                DialogUtil.show(mActivity, "是否退出登录？", null, new DialogUtil.OnClickListener() {
                    @Override
                    public void onclick(DialogInterface dialogInterface, int i) {
                        loginOut();
                        dialogInterface.dismiss();
                    }
                });
                break;
        }
    }


    private void loginOut() {
        Api.getDefault(UrlService.class).loginOut(UserInfo.loginToken,"android").compose(Api.handlerResult())
                .subscribe(new RequestCallBack<String>(mActivity, true) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtil.show("已退出登录");
                        UserInfo.destroyUserInfo();
                        MainActivity.startActivity(mActivity, 0);
                    }

                    @Override
                    public boolean isShowErrorMessage() {
                        return false;
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                        ToastUtil.show("已退出登录");
                        UserInfo.destroyUserInfo();
                        MainActivity.startActivity(mActivity, 0);
                    }
                    @Override
                    public void onNotLogon() {
                    }
                });
    }
}
