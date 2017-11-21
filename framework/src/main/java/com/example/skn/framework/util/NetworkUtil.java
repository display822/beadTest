package com.example.skn.framework.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.skn.framework.base.BaseApplication;

/**
 * Created by hf
 */

public class NetworkUtil {

    /**
     * 检查是否有网络
     */
    public static boolean isNetworkAvailable() {
        NetworkInfo info = getNetworkInfo();
        return info != null && info.isAvailable();
    }

    /**
     * 检查是否是WIFI
     */
    public static boolean isWifiNet( ) {
        NetworkInfo info = getNetworkInfo();
        if (isNetworkAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI)
                return true;
        }
        return false;
    }

    /**
     * 获取wifi信息
     */
    public static WifiInfo getWifiInfo() {
        WifiManager wifi_service = (WifiManager) BaseApplication.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifi_service.getConnectionInfo();
    }

    /**
     * 检查是否是移动网络
     */
    public static boolean isMobileNet( ) {
        NetworkInfo info = getNetworkInfo();
        if (isNetworkAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE)
                return true;
        }
        return false;
    }

    /**
     * 获取网络信息
     */
    private static NetworkInfo getNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getApp()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }




}
