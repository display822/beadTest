package com.beadwallet;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.skn.framework.base.BaseApplication;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.util.CrashHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by hf
 */
public class Application extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ViewTarget.setTagId(R.id.tag_glide);
        setBaseUrl();
        initShare();
        initUmengStatistics();
    }

    protected void setBaseUrl() {
        Api.setBaseUrl("http://m.beadwallet.com/loansupermarket-app/");//正式
//        Api.setBaseUrl("http://106.14.97.38:8060/loansupermarket-app/");//测试
    }

    public static String getShareUrl() {
        return  "http://m.beadwallet.com/loansupermarket/#/main/home";//正式
//        return "http://106.14.97.38/szqb/#/main/home";//测试
    }

    @BindingAdapter("img")
    public static void loadImg(ImageView img, String path) {
        Glide.with(img.getContext()).load(path).into(img);
    }

    @BindingAdapter("circleImg")
    public static void loadCircleImg(ImageView img, String path) {
        Glide.with(img.getContext()).load(path)
                .placeholder(R.drawable.ic_default_loans)
                .error(R.drawable.ic_default_loans)
                .bitmapTransform(new CropCircleTransformation(img.getContext()))
                .into(img);
    }

    @BindingAdapter("circleImgDetail")
    public static void loadCircleImgDetail(ImageView img, String path) {
        Glide.with(img.getContext()).load(path)
                .placeholder(R.drawable.ic_default_loans_detail)
                .error(R.drawable.ic_default_loans_detail)
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
        PlatformConfig.setQQZone("1105462537", "HR8fVsfyqNLu1hy4");
        PlatformConfig.setWeixin("wx3705832d33a9e807", "6b9309740a94665bb4ebbb9feecf04de");
//        PlatformConfig.setSinaWeibo("1635000357", "901e4011b61379675a4239d7aa158664", "http://www.beadwallet.com");
        UMShareAPI.get(this);
    }

    private void initUmengStatistics() {
        //是否开启错误日志统计
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

}
