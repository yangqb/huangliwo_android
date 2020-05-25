package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/11
 * time: 16:52
 * email: 694125155@qq.com
 */
public class InternationalPriceInfo implements Serializable {
    public String packName;
    public double price;
    public double tax;
    public String taxType;
    public double cPrice;
    public double cTax;
    public String cTaxType;
    public String cabin;
    public String cabinLevel;
    public String priceKey;
    public String domain;
    public String productTag;
    public LuggageDirectData luggageDirectData;
    public double zk;
}
