package com.bead.dreamwallet.entity;

import java.io.Serializable;

/**
 * Created by DOY on 2017/7/21.
 */
public class LoansTitleEntity implements Serializable {
    private String sortAllName;
    private int sortInfoId;

    public String getSortAllName() {
        return sortAllName;
    }

    public void setSortAllName(String sortAllName) {
        this.sortAllName = sortAllName;
    }

    public int getSortInfoId() {
        return sortInfoId;
    }

    public void setSortInfoId(int sortInfoId) {
        this.sortInfoId = sortInfoId;
    }
}
