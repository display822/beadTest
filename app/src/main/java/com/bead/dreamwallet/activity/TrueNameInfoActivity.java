package com.bead.dreamwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.util.StringUtil;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.bead.dreamwallet.R;
import com.bead.dreamwallet.entity.UserBean;

import java.util.HashMap;
import java.util.Map;


/**
 * 我的资料（完善个人信息）
 */
public class TrueNameInfoActivity extends BaseActivity {


    private EditText et_name;
    private EditText et_phone;
    private EditText et_idcard;
    private UserBean userBean;

    public static void startActivity(Context context, UserBean userBean) {
        Intent intent = new Intent(context, TrueNameInfoActivity.class);
        intent.putExtra("userBean", userBean);
        context.startActivity(intent);
    }

    @Override
    protected void initVar() {
        userBean = (UserBean) getIntent().getSerializableExtra("userBean");
        setContentView(R.layout.activity_true_name_info);
    }

    @Override
    protected void init() {
        ToolBarUtil.getInstance(this).setTitle("实名认证").build();

        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_idcard = (EditText) findViewById(R.id.et_idcard);

        if (userBean != null) {
            et_name.setText(userBean.getName());
            et_name.setEnabled(false);
            et_phone.setText(userBean.getPhone());
            et_phone.setEnabled(false);
            et_idcard.setText(userBean.getIdcard());
            et_idcard.setEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //提交
        submit();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initData() {

    }

    private void submit() {
        String name = et_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("真实姓名不能为空");
            return;
        }
        if (!StringUtil.checkTrueName(name)) {
            ToastUtil.show("请输入真实姓名");
            return;
        }
        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("手机号码不能为空");
            return;
        }
        if (!StringUtil.isMobile(phone)) {
            ToastUtil.show("请输入正确的手机号码");
            return;
        }
        String idcard = et_idcard.getText().toString().trim();
        if (TextUtils.isEmpty(idcard)) {
            ToastUtil.show("身份证号码不能为空");
            return;
        }
        if (!StringUtil.checkIdentityCard(idcard)) {
            ToastUtil.show("请输入正确的身份证号码");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("idCard", idcard);
        map.put("phone", phone);
//        Api.getDefault(UrlService.class).saveCustomerBaseInfo(map).compose(Api.handlerResult())
//                .subscribe(new RequestCallBack<String>(mActivity,true) {
//                    @Override
//                    public void onSuccess(String s) {
//                        ToastUtil.show(s);
//                        MainActivity.startActivity(mActivity, 1);
//                    }
//
//                    @Override
//                    public void onFailure(String code, String errorMsg) {
//                    }
//                });


    }

}
