package com.comtempwallet.entity;

import java.io.Serializable;

/**
 * Created by DOY on 2017/7/21.
 */
public class BannerEntity implements Serializable {


    /**
     * titleImg : http://waterelephant.oss-cn-shanghai.aliyuncs.com/loansupermarket/upload/comPanyPic/2017-08-02/5bda4f77-4332-44b9-8ccb-bde3b962763f.jpg
     * url :  https://t.ee6e.cn/wap/sms_sem?id=352&pic_id=4
     */

    private String titleImg;
    private String url;

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
