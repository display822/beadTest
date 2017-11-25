package com.dreamwallet.entity;

/**
 * Created by DOY on 2017/9/4.
 */
public class InformationListEntity {

    /**
     * img :
     * id : 238
     * author : 追梦钱包
     * title : 如何贷款才能更省钱？教你几个技巧
     * create_time : 1503995404000
     * discuss_count : 2
     * url :  5555
     */

    private String detail;
    private String img;
    private int id;
    private String author;
    private String title;
    private Long create_time;
    private int discuss_count;
    private String url;


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

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

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
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
