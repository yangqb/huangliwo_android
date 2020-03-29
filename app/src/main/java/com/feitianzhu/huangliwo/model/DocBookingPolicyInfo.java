package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/25
 * time: 17:50
 * email: 694125155@qq.com
 */
public class DocBookingPolicyInfo implements Serializable {
    public int xcd;
    public int cardType;
    public int maxAge;
    public int minAge;
    public boolean childBuyAdult;
    public boolean soloChild;
    public boolean shareShowAct;
}
