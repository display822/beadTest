package com.example.skn.framework.http;

import java.io.File;

/**
 * Created by DOY on 2017/8/30.
 */
public interface OnDownLoadCallBack {
    public void onStart();

    public void onNext(long schedule);

    public void onError(Throwable e);

    public void onCompleted(File file);

}
