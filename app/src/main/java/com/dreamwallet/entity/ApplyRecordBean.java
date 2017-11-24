package com.dreamwallet.entity;

/**
 * Created by DIY on 2017/9/8.
 */

public class ApplyRecordBean {

//    id
//    productId--产品id
//    productName--产品名称
//    productLogo -- logo
//    lendSpeed  -- 放款速度
//    dailyInterestRate -- 日利息
//    quato -- 放款额度
//    cycle -- 期限范围
//    label -- 标签

    private String label;
    private int id;
    private String dailyInterestRate;
    private int productId;
    private String quato;
    private String lendSpeed;
    private String cycle;
    private String productLogo;
    private String productName;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDailyInterestRate() {
        return dailyInterestRate;
    }

    public void setDailyInterestRate(String dailyInterestRate) {
        this.dailyInterestRate = dailyInterestRate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getQuato() {
        return quato;
    }

    public void setQuato(String quato) {
        this.quato = quato;
    }

    public String getLendSpeed() {
        return lendSpeed;
    }

    public void setLendSpeed(String lendSpeed) {
        this.lendSpeed = lendSpeed;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getProductLogo() {
        return productLogo;
    }

    public void setProductLogo(String productLogo) {
        this.productLogo = productLogo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
