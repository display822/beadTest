package com.beadwallet.entity;

import java.io.Serializable;

/**
 * Created by hf
 */
public class DetailsEntity implements Serializable {

    /**
     * id : 237
     * title :  2017中国产品经理大会 解码未来产品经理，9月23-24日即将在中国硅谷 • 深圳召开
     * titleImg :  http://waterelephant.oss-cn-shanghai.aliyuncs.com/loansupermarket/upload/comPanyPic/2017-08-28/2329de48-39d4-468e-ba7e-ea07e7355032.jpg
     * detail : 这一年 从VR、AI、共享单车、短视频到知识付费 行业间的对弈似乎从未停歇 “对战之火”燃遍全球 AI、大数据、云计算 不仅让AlphaGo打败柯洁 还让机器听懂了我们的语言 更让无人汽车驶入了我们的生活……
     * createTime : 1503891243000
     * author：
     */

    private String id;
    private String title;
    private String titleImg;
    private String author;
    private String detail;
    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
