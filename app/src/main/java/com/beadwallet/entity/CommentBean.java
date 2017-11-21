package com.beadwallet.entity;

/**
 * Created by DIY on 2017/9/7.
 */

public class CommentBean {

    /**
     * name : 15972132722
     * pid : 1
     * id : 1
     * time : 1504256014000
     * content : 123123
     */

    private String name;
    private int pid;
    private int id;
    private long time;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
