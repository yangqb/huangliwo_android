package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/14
 * time: 14:27
 * email: 694125155@qq.com
 */
public class BkPriceInfo implements Serializable {
    public String ticketPrice;
    public String price;
    public String barePrice;
    public String originalBarePrice;
    public String addPrice;
    public String basePrice;
    public String discount;
    public String childPrice;
    public String childTicketPrice;
    public String originalChildBarePrice;
    public String childAddPrice;
    public String babyPrice;
    public String babyTicketPrice;
    public String originalBabyBarePrice;
    public String babyAddPrice;
    public String returnMoney;
    public String cutMoney;
    public String babyServiceFee;
    public BkInventory inventory;
    public int tof;
    public int arf;
    public int childtof;
    public String prdTag;
    public String dtTag;
    public Map<String, List<BkPackageInfo>> priceTag;
    public Map<String, String> extMap;
    public boolean supportChildBuyAdult;
    public boolean supportChild;
}
