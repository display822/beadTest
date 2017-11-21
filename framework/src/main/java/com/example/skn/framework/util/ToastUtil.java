package com.example.skn.framework.util;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.example.skn.framework.base.BaseApplication;


public class ToastUtil {

    public static boolean isToast = true;
    private static String oldMsg;
    private static long time;


    public static void show(String msg) {
        if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(msg.trim())) {
            return;
        }
        Toast toast = Toast.makeText(BaseApplication.getApp(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        if (isToast) {
            if (!TextUtils.equals(msg, oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
                time = System.currentTimeMillis();
                toast.show();
            } else {
                // 显示内容一样时，只有间隔时间大于2秒时才显示
                if (System.currentTimeMillis() - time > 2000) {
                    time = System.currentTimeMillis();
                    toast.show();
                }
            }
            oldMsg = msg;
        }
    }

}
