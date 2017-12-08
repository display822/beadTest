package com.dreamwallet.entity;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class MoneyRecord {

    private int type;
    private int money;
    private String record_date;
    private String comment;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
