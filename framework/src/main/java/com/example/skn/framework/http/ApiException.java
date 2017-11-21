package com.example.skn.framework.http;

/**
 * Created by DOY on 2017/8/30.
 */
public class ApiException extends  Exception {
    private String code;

    public String getCode() {
        return code;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, String code) {
        super(message);
        this.code = code;
    }
}
