package com.dreamwallet.entity;

/**
 * Created by DOY on 2017/8/31.
 */
public class HomeInformationEntity {


    /**
     * img :  http://waterelephant.oss-cn-shanghai.aliyuncs.com/loansupermarket/upload/comPanyPic/2017-08-28/2329de48-39d4-468e-ba7e-ea07e7355032.jpg
     * id : 239
     * title : 马云称30年后孩子们找不到工作?专家反驳：不需要工作
     * url :  1233
     */

    private String img;
    private int id;
    private String title;
    private String url;
    private long create_time;

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

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
