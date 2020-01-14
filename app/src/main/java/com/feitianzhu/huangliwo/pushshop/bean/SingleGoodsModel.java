package com.feitianzhu.huangliwo.pushshop.bean;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/1/13
 * time: 18:04
 * email: 694125155@qq.com
 */
public class SingleGoodsModel implements Serializable {
    private int id;
    private String name;
    private double singlePrice;
    private int num;
    private int smId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(double singlePrice) {
        this.singlePrice = singlePrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSmId() {
        return smId;
    }

    public void setSmId(int smId) {
        this.smId = smId;
    }
}
