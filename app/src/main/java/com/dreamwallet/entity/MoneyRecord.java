package com.dreamwallet.entity;

import java.util.Date;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class MoneyRecord {

    private int type;
    private float money;
    private Date date;
    private String comment;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
