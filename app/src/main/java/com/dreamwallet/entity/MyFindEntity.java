package com.dreamwallet.entity;

import java.io.Serializable;

/**
 * Created by DOY on 2017/9/5.
 */
public class MyFindEntity implements Serializable {


//                create_time = 1503892568000,
//                detail = "    对于一家公司而言，股权激励到底有多重要此前...",
//                img = "http://img.yixieshi.com/wp-content/uploads/2017/08/1503765897-3733-26.jpg?imageView2/2/w/800/h/600/interlace/1",
//                informationId = 240,
//                title = "腾讯前CTO教你如何用股权激励来医治大公司病",
//                visitId = 76

    private String img;
    private int informationId;
    private int visitId;
    private String title;
    private String detail;
    private long create_time;

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MyFindEntity) {
            return ((MyFindEntity) obj).getInformationId() == this.informationId;
        }
        return super.equals(obj);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getInformationId() {
        return informationId;
    }

    public void setInformationId(int informationId) {
        this.informationId = informationId;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
