package com.dreamwallet;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.skn.framework.base.BaseApplication;
import com.example.skn.framework.http.Api;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by hf
 */
public class Application extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ViewTarget.setTagId(com.dreamwallet.R.id.tag_glide);
        JPushInterface.setDebugMode(false);
        JPushInterface.init(this);
        setBaseUrl();
        initShare();
        initUmengStatistics();
    }

    protected void setBaseUrl() {
        Api.setBaseUrl("http://m.dreamwallet.cn/loansupermarket-app/");//正式
//        Api.setBaseUrl("http://106.14.97.38:8060/loansupermarket-app/");//测试 n.b e a dwallet.com
    }

    public static String getShareUrl() {
        return  "http://m.dreamwallet.cn";//正式
//        return "http://106.14.97.38/szqb/#/main/home";//测试
    }

    @BindingAdapter("img")
    public static void loadImg(ImageView img, String path) {
        Glide.with(img.getContext()).load(path).into(img);
    }

    @BindingAdapter("circleImg")
    public static void loadCircleImg(ImageView img, String path) {
        Glide.with(img.getContext()).load(path)
                .placeholder(com.dreamwallet.R.drawable.logo_gray)
                .error(com.dreamwallet.R.drawable.logo_gray)
                .bitmapTransform(new CropCircleTransformation(img.getContext()))
                .into(img);
    }

    @BindingAdapter("circleImgDetail")
    public static void loadCircleImgDetail(ImageView img, String path) {
        Glide.with(img.getContext()).load(path)
                .placeholder(com.dreamwallet.R.drawable.logo_gray)
                .error(com.dreamwallet.R.drawable.logo_gray)
                .bitmapTransform(new CropCircleTransformation(img.getContext()))
                .into(img);
    }

    @BindingAdapter("time")
    public static void getTime(TextView tv, long time) {
        DateFormat df = new SimpleDateFormat("MM-dd hh:mm", Locale.CHINESE);
        tv.setText(df.format(time));
    }

    @BindingAdapter("date")
    public static void getDate(TextView tv, long time) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        tv.setText(df.format(time));
    }

    private void initShare() {
        Config.DEBUG = true;
        PlatformConfig.setQQZone("101445672", "6b94bb48484d12ecf698522134517c3b");
        PlatformConfig.setWeixin("wx6ca55a14e5ba1695", "91e09add8da0ed8a0e5de3a3fe6701cc");
//        PlatformConfig.setSinaWeibo("1635000357", "901e4011b61379675a4239d7aa158664", "http://www.beadwallet.com");
        UMShareAPI.get(this);
    }

    private void initUmengStatistics() {
        //是否开启错误日志统计
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

}
