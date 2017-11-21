package com.example.skn.framework.base;

import android.app.Activity;
import android.app.Application;

import com.example.skn.framework.R;
import com.example.skn.framework.util.AppUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hf
 */

abstract public class BaseApplication extends Application {

    private static BaseApplication app;
    private static RefWatcher refWatcher;
    private String channel;

    public List<Activity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        app = this;
        activityList = new ArrayList<>();
        refWatcher = LeakCanary.install(this);
        channel = AppUtil.getChannelName();
//        CrashHandler.getInstance(this);
    }

    public String getChannel() {
        return channel;
    }

    public static BaseApplication getApp() {
        return app;
    }

    public static RefWatcher getRefWatcher() {
        return refWatcher;
    }


    static {
        //static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater((context, layout) -> {
            layout.setPrimaryColorsId(android.R.color.transparent, R.color.color_424242);//全局设置主题颜色
            return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
        });
    }
}
