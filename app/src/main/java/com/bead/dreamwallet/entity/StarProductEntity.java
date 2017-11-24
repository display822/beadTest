package com.bead.dreamwallet.entity;

import java.io.Serializable;

/**
 * Created by hf
 */
public class StarProductEntity implements Serializable {


    /**
     * id
     * max_quato : 1111.0
     * lend_speed : 11
     * daily_interest_rate : 30.0
     * min_quato : 111.0
     * product_name : 借你花2
     * product_img :
     */
    private String id;
    private String product_img;
    private String lend_speed;
    private String daily_interest_rate;
    private  String quato;
    private String product_name;

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLend_speed() {
        return lend_speed;
    }

    public void setLend_speed(String lend_speed) {
        this.lend_speed = lend_speed;
    }

    public String getDaily_interest_rate() {
        return daily_interest_rate;
    }

    public void setDaily_interest_rate(String daily_interest_rate) {
        this.daily_interest_rate = daily_interest_rate;
    }

    public String getQuato() {
        return quato;
    }

    public void setQuato(String quato) {
        this.quato = quato;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
