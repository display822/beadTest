package com.beadwallet.util;


import com.example.skn.framework.util.SpUtil;

/**
 * Created by DOY on 2017/7/21.
 */
public class UserInfo {

    public static String pwd;
    public static String loginToken;
    public static String phone;
    public static String name;

    /**
     * 登录成功后保存信息入本地
     * 存入静态变量
     */
    public static void saveUserInfo(String mPhone, String mLoginToken, String passward) {
        SpUtil.setData("phone", mPhone);
        SpUtil.setData("loginToken", mLoginToken);
        SpUtil.setData("pwd", passward);
        pwd = passward;
        loginToken = mLoginToken;
        phone = mPhone;
    }

    /**
     * 销毁所有数据
     */
    public static void destroyUserInfo() {
        SpUtil.setData("phone", "");
        SpUtil.setData("loginToken", "");
        SpUtil.setData("pwd", "");
        SpUtil.setData("name", "");
        pwd = "";
        loginToken = "";
        phone = "";
        name = "";
    }

    public static void initUserInfo() {
        phone = SpUtil.getStringData("phone");
        loginToken = SpUtil.getStringData("loginToken");
        pwd = SpUtil.getStringData("pwd");
        name = SpUtil.getStringData("name");
    }

    public static boolean isFirst() {
        return "".equals(SpUtil.getStringData("loginToken"));
    }

    public static boolean isLogin() {
        return !isFirst();
    }
}
