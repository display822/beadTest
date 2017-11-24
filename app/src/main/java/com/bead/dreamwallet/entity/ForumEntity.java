package com.bead.dreamwallet.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hf
 */
public class ForumEntity implements Serializable {

    /**
     * imgUrl : 111111
     * num : 6
     * time : 1503978184000
     * title : 测试11
     */

    private String imgUrl;
    private int num;
    private long time;
    private String title;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
