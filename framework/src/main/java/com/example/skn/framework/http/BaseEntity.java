package com.example.skn.framework.http;

/**
 * Created by DOY on 2017/8/30.
 */
public class BaseEntity<T> {
    private String code;
    private String mesg;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
