package com.example.skn.framework.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.skn.framework.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DOY on 2017/7/11.
 */
public class AppUtil {


    public static int getWidth(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static String getVersionName() {
        try {
            String pkName = BaseApplication.getApp().getPackageName();
            return BaseApplication.getApp().getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getVersionCode() {
        try {
            String pkName = BaseApplication.getApp().getPackageName();
            return BaseApplication.getApp().getPackageManager().getPackageInfo(pkName, 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String getPackageName() {
        try {
            return BaseApplication.getApp().getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void destroyApp() {
        for (Activity item : BaseApplication.getApp().activityList) {
            if (item != null) {
                item.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());   //获取PID
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = BaseApplication.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = BaseApplication.getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getChannelName() {
        String channelName = null;
        try {
            PackageManager packageManager = BaseApplication.getApp().getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(BaseApplication.getApp().getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }


    public static void gotoAppStoreDetail(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            List<String> pkgList = getFilterInstallMarkets(context, getInstallAppMarkets(context));
            String marketPkg = "";
            String channel = getChannelName();
            if (TextUtils.equals(channel, "xiaomi")) {
                marketPkg = "com.xiaomi.market";//小米
            } else if (TextUtils.equals(channel, "huawei")) {
                marketPkg = "com.huawei.appmarket";//华为
            } else if (TextUtils.equals(channel, "vivo")) {
                marketPkg = "com.bbk.appstore";//vivo
            } else if (TextUtils.equals(channel, "tencent")) {
                marketPkg = "com.tencent.android.qqdownloader";//腾讯应用宝
            } else if (TextUtils.equals(channel, "zs360")) {
                marketPkg = "com.qihoo.appstore";//360手机助手
            } else if (TextUtils.equals(channel, "anzhi")) {
                marketPkg = "com.hiapk.marketpho";//安智应用商店
            } else if (TextUtils.equals(channel, "nearme")) {
                marketPkg = "com.oppo.market";//oppo应用商店
            } else if (TextUtils.equals(channel, "meizu")) {
                marketPkg = "com.meizu.mstore";//魅族应用商店
            } else if (TextUtils.equals(channel, "gfan")) {
                marketPkg = "com.mappn.gfan";//机锋应用市场
            } else if (TextUtils.equals(channel, "yingyonghui")) {
                marketPkg = "com.yingyonghui.market";//应用汇
            }
            if (pkgList != null && pkgList.size() > 0 && pkgList.contains(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.show("您的手机没有安装android应用市场");
            e.printStackTrace();
        }
    }

    /**
     * 过滤出已经安装的包名集合
     *
     * @param context * @param pkgs 待过滤包名集合
     * @return 已安装的包名集合
     */
    public static ArrayList<String> getFilterInstallMarkets(Context context, ArrayList<String> pkgs) {
        ArrayList<String> appList = new ArrayList<String>();
        if (context == null || pkgs == null || pkgs.size() == 0) return appList;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = pkgs.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = pkgs.get(j);
                PackageInfo packageInfo = installedPkgs.get(i);
                try {
                    installPkg = packageInfo.packageName;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg)) continue;
                if (installPkg.equals(checkPkg)) {
                    appList.add(installPkg);
                    break;
                }
            }
        }
        return appList;
    }


    /**
     * 获取已安装应用商店的包名列表
     * 获取有在AndroidManifest 里面注册<category android:name="android.intent.category.APP_MARKET" />的app
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getInstallAppMarkets(Context context) { //默认的应用市场列表，有些应用市场没有设置APP_MARKET通过隐式搜索不到
        ArrayList<String> pkgList = new ArrayList<>();
        pkgList.add("com.xiaomi.market");
        pkgList.add("com.huawei.appmarket");
        pkgList.add("com.bbk.appstore");
        pkgList.add("com.qihoo.appstore");
        pkgList.add("com.meizu.mstore");
        pkgList.add("com.hiapk.marketpho");
        pkgList.add("com.yingyonghui.market");
        pkgList.add("com.oppo.market");
        pkgList.add("com.mappn.gfan");
        pkgList.add("com.sec.android.app.samsungapps");
        pkgList.add("com.tencent.android.qqdownloader");
        ArrayList<String> pkgs = new ArrayList<String>();
        if (context == null) return pkgs;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        if (infos == null || infos.size() == 0) return pkgs;
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName)) pkgs.add(pkgName);
        }
        //取两个list并集,去除重复
        pkgList.removeAll(pkgs);
        pkgs.addAll(pkgList);
        return pkgs;
    }


    public static void startWeb(Context context, String url) {
        try {
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("beadwallet", "startWeb----------->" + e.getMessage());
        }
    }
}
