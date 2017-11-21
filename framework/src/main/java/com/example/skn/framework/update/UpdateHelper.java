package com.example.skn.framework.update;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.skn.framework.util.AppUtil;

import ezy.boost.update.IUpdateParser;
import ezy.boost.update.OnFailureListener;
import ezy.boost.update.UpdateError;
import ezy.boost.update.UpdateInfo;
import ezy.boost.update.UpdateManager;

/**
 * Created by hf on 2017/9/6.
 */
public class UpdateHelper {
    public static void update(Context context, String url, String channel) {
        UpdateManager.create(context).setUrl(url).setPostData("versionId=1&storeNameChannel=" + channel).setParser(new IUpdateParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                UpdateEntity baseEntity = JSONObject.parseObject(source, UpdateEntity.class);
                UpdateInfo info = new UpdateInfo();
                if (baseEntity != null && TextUtils.equals(baseEntity.getCode(), "000")) {
                    UpdateEntity.DataBean updateEntity = baseEntity.getData();
                    if (updateEntity != null) {
                        int newVersionName = Integer.parseInt(updateEntity.getAppVersion().replaceAll("\\.", ""));
                        int currentVersionName = Integer.parseInt(AppUtil.getVersionName().replaceAll("\\.", ""));
                        info.hasUpdate = newVersionName > currentVersionName;//是否有新版本
                        info.isForce = updateEntity.isNeedUpdate();//是否强制更新
                        info.isIgnorable = false;//是否忽略
                        info.versionCode = newVersionName;
                        info.versionName = updateEntity.getAppVersion();
                        if (TextUtils.isEmpty(updateEntity.getUpdateContent())) {
                            info.updateContent = "";
                        } else {
                            info.updateContent = updateEntity.getUpdateContent();
                        }
                        info.url = updateEntity.getApkDownPath();
                        info.size = updateEntity.getSize();
                        info.md5 = updateEntity.getFile_MD5();
                    }
                }
                return info;
            }
        }).setOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(UpdateError error) {
                                        Log.i("UpdateHelper", error.getMessage());
                                    }
                                }
        ).check();
    }


}
