package com.dreamwallet.entity;

import java.io.Serializable;

/**
 * Created by DOY on 2017/8/31.
 */
public class InformationEntity implements Serializable {

    /**
     *  img :  http://waterelephant.oss-cn-shanghai.aliyuncs.com/loansupermarket/upload/comPanyPic/2017-08-28/2329de48-39d4-468e-ba7e-ea07e7355032.jpg
     *  id : 238
     * author : admin
     *  title : 世界500强企业招人标准曝光:非985毕业生，简历直接扔垃圾桶
     * create_time : 1503995404000
     * discuss_count : 2
     * url :  5555
     */

    private String img;
    private int id;
    private String author;
    private String title;
    private long create_time;
    private int discuss_count;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getDiscuss_count() {
        return discuss_count;
    }

    public void setDiscuss_count(int discuss_count) {
        this.discuss_count = discuss_count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
