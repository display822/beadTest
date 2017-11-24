package com.dreamwallet.entity;

import java.io.Serializable;

/**
 * Created by DOY on 2017/7/21.
 */
public class LoginEntity implements Serializable {

    /**
     * pwd : string
     * token : string
     */

    private String pwd;
    private String token;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
