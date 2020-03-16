package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/14
 * time: 14:32
 * email: 694125155@qq.com
 */
public class BkPolicyInfo implements Serializable {
    public int cardType;
    public int maxAge;
    public int minAge;
    public String specialRule;
    public boolean childBuyAdult;
    public boolean cbaAddConFuelTax;
}
