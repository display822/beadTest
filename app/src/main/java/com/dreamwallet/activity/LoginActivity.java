package com.dreamwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.dreamwallet.R;
import com.dreamwallet.databinding.ActivityLoginBinding;
import com.dreamwallet.entity.LoginEntity;
import com.dreamwallet.entity.UserBean;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;
import com.dreamwallet.widget.OnNoDoubleClickListener;
import com.dreamwallet.widget.TimeButton;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.base.BaseWebViewActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.BaseEntity;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.SpUtil;
import com.example.skn.framework.util.StringUtil;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;


public class LoginActivity extends BaseActivity implements TimeButton.OnLoadDataListener {
    private ActivityLoginBinding bind;
    private int loginType;
    public final static String FINISH = "finish";
    public final static String TO_MAIN = "to_main";
    private String type;


    public static void startActivity(Activity activity, String type) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    @Override
    protected void initVar() {
        setFlagTranslucentStatus();
        loginType = 2;
        type = getIntent().getStringExtra("type");
    }

    @Override
    protected void init() {
        bind = DataBindingUtil.setContentView(mActivity, R.layout.activity_login);
        ToolBarUtil.getInstance(mActivity).setTitle("登录").build();
        if (!"".equals(SpUtil.getStringData("histore_phone"))) {
            bind.etPhone.setText(SpUtil.getStringData("histore_phone"));
        }
        bind.tvLoginAgreement.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        bind.tbCode.setOnLoadDataListener(this);
        bind.setClearPhoneListener(v -> bind.etPhone.setText(""));
        bind.setPwdLoginListener(v -> {
            loginType = 1;
            Click();
        });
        bind.setCodeLoginListener(v -> {
            loginType = 2;
            Click();
        });
        bind.setLoginListener(v -> {
            if (checkLoginInfo()) login();
        });
        bind.setFindPwdListener(v -> {
            FindBackPasswordActivity.startActivity(mActivity);
        });
        bind.tvLoginAgreement.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                BaseWebViewActivity.show(mActivity, "file:///android_asset/register.html", "追梦钱包注册协议", true);
            }
        });
    }

    @Override
    protected void initData() {

    }


    @Override
    public void load(final TimeButton timeButton) {
        String phone = bind.etPhone.getText().toString().trim();
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
        map.put("smsType", 1);
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

    public void Click() {

        if (loginType == 1) {
            bind.imgLine2.setVisibility(View.VISIBLE);
            bind.imgLine1.setVisibility(View.GONE);

            //密码登录
            new Handler().postDelayed(() -> {
                bind.etPwd.setVisibility(View.VISIBLE);
                bind.llLastPwd.setVisibility(View.VISIBLE);
                bind.llCode.setVisibility(View.GONE);
            }, 500);
        }else if (loginType == 2) {
            bind.imgLine1.setVisibility(View.VISIBLE);
            bind.imgLine2.setVisibility(View.GONE);

            new Handler().postDelayed(() -> {
                bind.etPwd.setVisibility(View.GONE);
                bind.llLastPwd.setVisibility(View.GONE);
                bind.llCode.setVisibility(View.VISIBLE);
            }, 500);
        }
    }

    private boolean checkLoginInfo() {
        String phone = bind.etPhone.getText().toString().trim();
        String pwd = bind.etPwd.getText().toString().trim();
        String code = bind.etCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("手机号码不能为空");
            return false;
        }
        if (!StringUtil.isMobile(phone)) {
            ToastUtil.show("请输入正确的手机号");
            return false;
        }
        if (loginType == 1) {
            if (TextUtils.isEmpty(pwd)) {
                ToastUtil.show("密码不能为空");
                return false;
            } else if (!StringUtil.checkPasswordNumOrChar(pwd)) {
                ToastUtil.show("请输入6~16位字符的密码");
                return false;
            }
        } else if (loginType == 2 && TextUtils.isEmpty(code)) {
            ToastUtil.show("验证码不能为空");
            return false;
        }
        if (!bind.cbAgreement.isChecked()) {
            ToastUtil.show("请同意追梦钱包注册协议");
            return false;
        }
        return true;
    }

    private void login() {
        SpUtil.setData("histore_phone", bind.etPhone.getText().toString().trim());
        Map<String, Object> map = new HashMap<>();
        map.put("phone", bind.etPhone.getText().toString().trim());
        Observable<BaseEntity<LoginEntity>> login;
        if (loginType == 1) {
            map.put("pwd", bind.etPwd.getText().toString().trim());
            login = Api.getDefault(UrlService.class).login(map);
        } else {
            map.put("code", bind.etCode.getText().toString().trim());
            login = Api.getDefault(UrlService.class).codeLogin(map);
        }
        login.compose(Api.handlerResult())
                .subscribe(new RequestCallBack<LoginEntity>(mActivity, true) {
                    @Override
                    public void onSuccess(LoginEntity loginEntity) {
                        if (loginEntity != null) {
                            UserInfo.saveUserInfo(bind.etPhone.getText().toString().trim(), loginEntity.getToken(), loginEntity.getPwd());
                            getUserInfo();
                            if (type.equals(TO_MAIN)) MainActivity.startActivity(mActivity, 0);
                            else finish();
                        }
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (type.equals(TO_MAIN)) MainActivity.startActivity(mActivity, 0);
        else {
//            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    private void getUserInfo() {
        Api.getDefault(UrlService.class).queryUser(UserInfo.loginToken).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<UserBean>(mActivity, false) {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        if (userBean != null) {
                            UserInfo.name = userBean.getName();
                        }
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                    }
                });
    }
}
