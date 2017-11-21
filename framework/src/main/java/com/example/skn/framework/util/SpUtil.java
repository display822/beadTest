package com.example.skn.framework.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.skn.framework.base.BaseApplication;

/**
 * Created by DOY on 2017/4/25.
 */
public class SpUtil {


    public static String getStringData(String key) {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getApp()).getString(key, "");

    }

    public static boolean getBooleanData(String key) {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getApp()).getBoolean(key, false);

    }

    public static int getIntData(String key) {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getApp()).getInt(key, 0);

    }

    public static long getLongData(String key) {
        return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getApp()).getLong(key, 0L);

    }

    public static void setData(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getApp()).edit();
        editor.putString(key, value);
        editor.apply();

    }

    public static void setData(String key, int value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getApp()).edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public static void setData(String key, long value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getApp()).edit();
        editor.putLong(key, value);
        editor.apply();

    }

    public static void setData(String key, boolean value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getApp()).edit();
        editor.putBoolean(key, value);
        editor.apply();

    }


}
