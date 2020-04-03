package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/3
 * time: 20:57
 * email: 694125155@qq.com
 */
public class BasePassengerPriceInfo implements Serializable {
    public boolean disabled;
    public String disableReason;
    public long passengerId;
    public String passengerName;
    public String cardNum;
    public String passengerTypeStr;
    public int ticketPrice;
    public int constructionFee;
    public int fuelTax;
}
