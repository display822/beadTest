package com.bead.dreamwallet.entity;

/**
 * Created by DIY on 2017/7/24.
 */
public class BorrowInfoBean {

   private String borrowerId;
   private String expectedAmount;
   private String borrowCycle;
   private String paymentDate;

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(String expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public String getBorrowCycle() {
        return borrowCycle;
    }

    public void setBorrowCycle(String borrowCycle) {
        this.borrowCycle = borrowCycle;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }
}
