package com.dreamwallet.entity;

import java.io.Serializable;

/**
 * Created by DIY on 2017/7/21.
 */

public class UserBaseBean implements Serializable{


    public UserBaseBean() {
    }

    public UserBaseBean(String idCard, String name) {
        this.idCard = idCard;
        this.name = name;
    }

    /**
     * idCard :
     * name :
     */


    private String idCard;
    private String name;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
