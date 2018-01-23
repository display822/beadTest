package com.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.dialog.DialogUtil;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.util.*;
import com.dreamwallet.R;
import com.dreamwallet.entity.BorrowInfoBean;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class ApplyMoneyActivity extends BaseActivity {


    private EditText et_money;
    private EditText et_period;
    private EditText et_time;
    private TextView btn_apply_money;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ApplyMoneyActivity.class));
    }


    @Override
    protected void initVar() {
        setContentView(R.layout.activity_apply_money);
    }

    @Override
    protected void init() {
        ToolBarUtil.getInstance(this).setTitle("填写借款资料").build();
        et_money = (EditText) findViewById(R.id.et_money);
        et_period = (EditText) findViewById(R.id.et_period);
        et_time = (EditText) findViewById(R.id.et_time);
        btn_apply_money = (TextView) findViewById(R.id.btn_apply_money);

        et_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString()) && Float.parseFloat(editable.toString()) > 100000) {
                    ToastUtil.show("期望金额只能在100~100000，请重新输入~~");
                    et_money.setText("");
                }
            }
        });
        et_period.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString()) && Float.parseFloat(editable.toString()) > 200) {
                    ToastUtil.show("借款周期只能在0~200，请重新输入~~");
                    et_period.setText("");
                }
            }
        });
        et_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString()) && Float.parseFloat(editable.toString()) > 100) {
                    ToastUtil.show("到账时间只能在1~100，请重新输入~~");
                    et_time.setText("");
                }
            }
        });
        btn_apply_money.setOnClickListener(view -> submit());

    }

    @Override
    protected void initData() {
        Map<String, Object> map = new HashMap<>();
        map.clear();
        map.put("loginToken", UserInfo.loginToken);
        DialogUtil.showLoading(this);
        Api.getDefault(UrlService.class).getBorrowInfo(map, "android").compose(Api.handlerResult()).subscribe(new RequestCallBack<BorrowInfoBean>(mActivity,true) {
            @Override
            public void onSuccess(BorrowInfoBean borrowInfoBean) {
                if (borrowInfoBean != null) {
                    if (!TextUtils.isEmpty(borrowInfoBean.getExpectedAmount())) {
                        et_money.setText(borrowInfoBean.getExpectedAmount());
                    }
                    if (!TextUtils.isEmpty(borrowInfoBean.getBorrowCycle())) {
                        et_period.setText(borrowInfoBean.getBorrowCycle());
                    }
                    if (!TextUtils.isEmpty(borrowInfoBean.getPaymentDate())) {
                        et_time.setText(borrowInfoBean.getPaymentDate());
                    }
                    if (!TextUtils.isEmpty(et_money.getText().toString())) {
                        et_money.setSelection(et_money.getText().length());
                    }
                }
            }

            @Override
            public void onFailure(String code, String errorMsg) {

            }
        });
    }

    private void submit() {
        String money = et_money.getText().toString().trim();
        if (TextUtils.isEmpty(money) || Float.parseFloat(money) < 10 || Float.parseFloat(money) > 100000) {
            ToastUtil.show("期望金额只能在100~100000，请重新输入~~");
            return;
        }

        String period = et_period.getText().toString().trim();
        if (TextUtils.isEmpty(period) || Float.parseFloat(period) <= 0 || Float.parseFloat(period) > 200) {
            ToastUtil.show("借款周期只能在0~200，请重新输入~~");
            return;
        }

        String time = et_time.getText().toString().trim();
        if (TextUtils.isEmpty(time) || Float.parseFloat(time) < 1 || Float.parseFloat(time) > 100) {
            ToastUtil.show("到账时间只能在1~100，请重新输入~~");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("loginToken", UserInfo.loginToken);
//        map.put("borrowerId", UserInfo.borrowerId);
        map.put("expectedAmount", (int) Float.parseFloat(money));
        map.put("borrowCycle", (int) Float.parseFloat(period));
        map.put("paymentDate", (int) Float.parseFloat(time));
        DialogUtil.showLoading(this);
        Api.getDefault(UrlService.class).saveBorrowInfo(map, "android").compose( Api.handlerResult())
                .subscribe(new RequestCallBack<String>(mActivity,true) {
                    @Override
                    public void onSuccess(String s) {
                       // ToastUtil.show(s);
                        //跳转到推荐产品页面
                        MainActivity.startActivity(mActivity, 1);
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                    }
                });

    }
}
