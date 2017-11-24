package com.dreamwallet.entity;

import java.io.Serializable;

/**
 * Created by DIY on 2017/9/6.
 */

public class UserBean implements Serializable{


    /**
     * borrowerId : 0
     * idcard : string
     * name : string
     * phone : string
     */

    private long borrowerId;
    private String idcard;
    private String name;
    private String phone;

    public long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
