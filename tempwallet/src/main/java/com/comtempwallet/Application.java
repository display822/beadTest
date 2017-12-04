package com.comtempwallet;

import com.example.skn.framework.base.BaseApplication;
import com.example.skn.framework.http.Api;

/**
 * Created by Administrator on 2017/12/2 0002.
 *
 */

public class Application extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        setBaseUrl();

    }

    protected void setBaseUrl() {
        Api.setBaseUrl("http://");//正式
    }
}
