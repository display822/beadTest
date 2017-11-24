package com.dreamwallet.entity;

import java.io.Serializable;

/**
 * Created by DOY on 2017/8/31.
 */
public class MyActivityEntity implements Serializable {

    /**
     *  img :  http://waterelephant.oss-cn-shanghai.aliyuncs.com/loansupermarket/upload/comPanyPic/2017-08-28/2329de48-39d4-468e-ba7e-ea07e7355032.jpg
     *  id : 238
     *  title : 共享单车围城，叫停投放不是唯一的破局思路
     * url :  5555
     */

    private String img;
    private int id;
    private String title;
    private String url;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
