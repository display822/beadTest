package com.example.skn.framework.http;

import android.accounts.NetworkErrorException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.dialog.DialogUtil;
import com.example.skn.framework.util.ToastUtil;

import java.net.ConnectException;
import java.util.concurrent.TimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by DOY on 2017/8/30.
 */
public abstract class RequestCallBack<T> extends Subscriber<T> {
    private Context context;
    private boolean isShowDialog;

    //isShow  是否显示dialog 必须传入context
    //context  是否进入登录界面
    public RequestCallBack(Context context, boolean isShowDialog) {
        this.context = context;
        this.isShowDialog = isShowDialog;
    }

    public RequestCallBack(Context context) {
        this.context = context;
        this.isShowDialog = false;
    }

    @Override
    public void onStart() {
        if (isShowDialog)
            DialogUtil.showLoading(context);
    }

    @Override
    public void onCompleted() {
        if (isShowDialog)
            DialogUtil.cancel();
    }

    @Override
    public void onError(Throwable e) {
        if (isShowDialog)
            DialogUtil.cancel();
        String ERROR_CODE = "0";
        if (e instanceof ApiException) {
            ERROR_CODE = ((ApiException) e).getCode();
            if (ERROR_CODE.equals("444")) {
                onNotLogon();
            }
        } else if (e instanceof NetworkErrorException || e instanceof ConnectException) {
            ERROR_CODE = "-1";
        }
        Log.e("beadwallet", "----error------->" + e.getMessage());
        String msg = getMessage(e);
        onFailure(ERROR_CODE, msg);
        if (isShowErrorMessage()) {
            ToastUtil.show(msg);
        }
    }

    public boolean isShowErrorMessage() {
        return true;
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(String code, String errorMsg);

    public void onNotLogon() {
        if (context != null) {
            ComponentName componentName = new ComponentName("com.beadwallet", "com.beadwallet.activity.LoginActivity");
            Intent intent = new Intent();
            intent.setComponent(componentName);
            intent.putExtra("type", "finish");
            if (context instanceof AppCompatActivity) {
                ((BaseActivity) context).startActivityForResult(intent, 32);
            } else {
                context.startActivity(intent);
            }
        }
    }

    private String getMessage(Throwable throwable) {
        if (throwable == null) return "";
        if (throwable instanceof TimeoutException)
            return "连接超时";
        else if (throwable instanceof HttpException)
            return "页面找不到";
        else if (throwable instanceof NetworkErrorException)
            return "网络错误";
        else if (throwable instanceof ConnectException)
            return "连接错误";
        else if (throwable instanceof ApiException)
            return throwable.getMessage();
        else
            return "网络异常";
    }

}
