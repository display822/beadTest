package com.example.skn.framework.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.skn.framework.R;

/**
 * Created by hf
 */

public class DialogUtil {




    private static Dialog dialog;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void showLoading(Context context) {
        if (dialog == null) {
            dialog = new Dialog(context, R.style.current_dialog);
            dialog.setContentView(R.layout.layout_loading);
            dialog.setCancelable(false);
        }
        if (!dialog.isShowing())
            dialog.show();
    }

    public static void cancel() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }


    /**
     * 我知道了
     */
    public static AlertDialog show(Activity activity, String msg, OnClickListener onSure) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(msg)
                .setTitle("提示")
                .setPositiveButton("我知道了", onSure::onclick)
                .setCancelable(false)
                .create();
        dialog.show();
        return dialog;
    }

    /**
     * 确定 and 取消
     */
    public static AlertDialog show(Activity activity, String msg, OnClickListener onCancel, OnClickListener onSure) {
        if (onSure == null) onSure = (dialogInterface, i) -> dialogInterface.dismiss();
        if (onCancel == null) onCancel = (dialogInterface, i) -> dialogInterface.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setMessage(msg)
                .setTitle("提示")
                .setPositiveButton("确定", onSure::onclick)
                .setNegativeButton("取消", onCancel::onclick)
                .setCancelable(false)
                .create();
        dialog.show();
        return dialog;
    }

    public interface OnClickListener {
        void onclick(DialogInterface dialogInterface, int i);
    }

}
