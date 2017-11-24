package com.bead.dreamwallet.util;

import android.util.Log;

import com.example.skn.framework.util.ToastUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by DOY on 2017/8/24.
 */
public class ShareListener implements UMShareListener {
    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        ToastUtil.show("分享成功");
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        ToastUtil.show("分享失败");
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        Log.i("UmShare", "onCancel");
    }
}
