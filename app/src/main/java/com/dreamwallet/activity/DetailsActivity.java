package com.dreamwallet.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;

import com.dreamwallet.R;
import com.dreamwallet.databinding.ActivityDetailsBinding;
import com.dreamwallet.entity.DetailsEntity;
import com.dreamwallet.util.ShareUtil;
import com.dreamwallet.util.StatisticsUtil;
import com.dreamwallet.util.UrlService;
import com.dreamwallet.util.UserInfo;
import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.umeng.socialize.UMShareAPI;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by hf
 * 资讯详情
 */
public class DetailsActivity extends BaseActivity {

    private ActivityDetailsBinding binding;
    private int cmsId;

    public static void startActivity(Context context, int cmsId) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("cmsId", cmsId);
        context.startActivity(intent);
    }

    @Override
    protected void initVar() {
        setFlagTranslucentStatus();
        cmsId = getIntent().getIntExtra("cmsId", 0);
    }

    @Override
    protected void init() {
        binding = DataBindingUtil.setContentView(mActivity, R.layout.activity_details);
        binding.titleBack.setOnClickListener(view -> finish());
        binding.titleShare.setOnClickListener(view -> {
            //分享
            doShare();
        });
    }

    @Override
    protected void initData() {
        Api.getDefault(UrlService.class).getCmsDesc(cmsId, "android").compose(Api.handlerResult())
                .subscribe(new RequestCallBack<DetailsEntity>(mActivity, true) {
                    @Override
                    public void onSuccess(DetailsEntity detailsEntity) {
                        if (detailsEntity != null) {
                            binding.setData(detailsEntity);
                            //设置webview
                            StatisticsUtil.setWebData(binding.content, detailsEntity.getDetail());
                        }
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {

                    }
                });
    }

    private void doShare(){
        if (UserInfo.isLogin()) {
            if (Build.VERSION.SDK_INT >= 23) {
                String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
                boolean flag = EasyPermissions.hasPermissions(mActivity, mPermissionList);
                if (flag) {
                    ShareUtil.ShareWeb(mActivity);
                } else {
                    EasyPermissions.requestPermissions(mActivity, "为保证分享的成功，请同意以下权限", 123, mPermissionList);
                }
            } else {
                ShareUtil.ShareWeb(mActivity);
            }
        } else {
            LoginActivity.startActivity(mActivity, LoginActivity.FINISH);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            ShareUtil.ShareWeb(mActivity);
        }
    }
}
