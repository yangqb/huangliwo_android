package com.feitianzhu.fu700.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/24
 * time: 17:32
 * email: 694125155@qq.com
 */
public class BalanceModel implements Serializable {

    /**
     * waitRelease : 11
     * totalAmount : 0
     * balance : 132
     */

    private double waitRelease;
    private double totalAmount;
    private double balance;

    public double getWaitRelease() {
        return waitRelease;
    }

    public void setWaitRelease(double waitRelease) {
        this.waitRelease = waitRelease;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
