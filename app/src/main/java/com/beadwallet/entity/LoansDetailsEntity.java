package com.beadwallet.entity;

import java.io.Serializable;

/**
 * Created by DOY on 2017/7/21.
 */
public class LoansDetailsEntity implements Serializable {
    private String dailyInterestRate;
    private String requireDocument;
    private String advantage;
    private String quato;
    private String maxCycle;
    private String link;
    private String logo;
    private String abbreviation;
    private String introduction;
    private String statistics;
    private String applyCondition;
    private int isFirst;

    public String getDailyInterestRate() {
        return dailyInterestRate;
    }

    public void setDailyInterestRate(String dailyInterestRate) {
        this.dailyInterestRate = dailyInterestRate;
    }

    public String getRequireDocument() {
        return requireDocument;
    }

    public void setRequireDocument(String requireDocument) {
        this.requireDocument = requireDocument;
    }

    public String getAdvantage() {
        return advantage;
    }

    public void setAdvantage(String advantage) {
        this.advantage = advantage;
    }

    public String getQuato() {
        return quato;
    }

    public void setQuato(String quato) {
        this.quato = quato;
    }

    public String getMaxCycle() {
        return maxCycle;
    }

    public void setMaxCycle(String maxCycle) {
        this.maxCycle = maxCycle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    public String getApplyCondition() {
        return applyCondition;
    }

    public void setApplyCondition(String applyCondition) {
        this.applyCondition = applyCondition;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }
}
