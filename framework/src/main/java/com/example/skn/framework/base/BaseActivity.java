package com.example.skn.framework.base;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ViewStubCompat;
import android.view.View;
import android.view.WindowManager;

import com.example.skn.framework.R;
import com.example.skn.framework.dialog.DialogUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by hf
 */

abstract public class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    protected BaseActivity mActivity;
    protected View mEmptyView;
    protected ViewStubCompat mEmptyStub;
    protected View.OnClickListener onClickListener;
    protected boolean isInitEmptyView;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        addActivity();
        setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initVar();
        init();
        initData();
    }

    abstract protected void initVar();

    abstract protected void init();

    abstract protected void initData();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 设置为沉浸式状态栏
     */
    protected void setFlagTranslucentStatus() {
        View decorview = getWindow().getDecorView();
        if(Build.VERSION.SDK_INT>=21){//5.0以上的系统支持
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_STABLE;//表示让应用主题内容占据系统状态栏的空间

            decorview.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.parseColor("#00ffffff"));//设置状态栏颜色为透明
        }

    }

    /**
     * 设置为全屏
     */
    protected void setFlagFullscreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
    }

    /**
     * 设置竖屏/横屏
     */
    private void setScreenOrientation(int type) {
        if (getRequestedOrientation() != type) {
            setRequestedOrientation(type);
        }
    }

    private void addActivity() {
        if (!BaseApplication.getApp().activityList.contains(this)) {
            BaseApplication.getApp().activityList.add(this);
        }

    }

    private void removeActivity() {
        if (BaseApplication.getApp().activityList.contains(this)) {
            BaseApplication.getApp().activityList.remove(this);
        }
    }

    @Override
    public void onBackPressed() {
        DialogUtil.cancel();
        super.onBackPressed();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseApplication.getRefWatcher().watch(this);
        removeActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    public void initEmptyOrNetErrorView(View rootView, int emptyOrNetErrorRes, View.OnClickListener onClickListener) {
        View emptyView = rootView.findViewById(emptyOrNetErrorRes);
        if (emptyView instanceof ViewStubCompat) {
            mEmptyStub = (ViewStubCompat) emptyView;
        } else {
            mEmptyView = emptyView;
        }
        this.onClickListener = onClickListener;
        isInitEmptyView = true;
    }

    public void updateEmptyOrNetErrorView(boolean hasData, boolean network) {
        if (isInitEmptyView) {
            if (!hasData) {
                if (mEmptyView == null && mEmptyStub != null) {
                    mEmptyView = mEmptyStub.inflate();
                }
                if (mEmptyView != null) {
                    View ll_net_error = mEmptyView.findViewById(R.id.ll_net_error);
                    View empty = mEmptyView.findViewById(R.id.tv_empty);
                    if (!network) {
                        View viewById = mEmptyView.findViewById(R.id.tv_reload);
                        if (viewById != null) {
                            viewById.setOnClickListener(onClickListener);
                        }
                        ll_net_error.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        ll_net_error.setVisibility(View.GONE);
                    }
                    mEmptyView.setVisibility(View.VISIBLE);

                }
            } else {
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        }
    }

}
