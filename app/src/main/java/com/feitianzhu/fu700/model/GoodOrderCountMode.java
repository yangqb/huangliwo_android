package com.feitianzhu.fu700.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/21
 * time: 14:48
 * email: 694125155@qq.com
 */
public class GoodOrderCountMode implements Serializable {

    /**
     * waitPay : 17
     * waitDeliver : 2
     * waitReceiving : 1
     * waitEval : 25
     */

    private int waitPay;
    private int waitDeliver;
    private int waitReceiving;
    private int waitEval;

    public int getWaitPay() {
        return waitPay;
    }

    public void setWaitPay(int waitPay) {
        this.waitPay = waitPay;
    }

    public int getWaitDeliver() {
        return waitDeliver;
    }

    public void setWaitDeliver(int waitDeliver) {
        this.waitDeliver = waitDeliver;
    }

    public int getWaitReceiving() {
        return waitReceiving;
    }

    public void setWaitReceiving(int waitReceiving) {
        this.waitReceiving = waitReceiving;
    }

    public int getWaitEval() {
        return waitEval;
    }

    public void setWaitEval(int waitEval) {
        this.waitEval = waitEval;
    }
}
