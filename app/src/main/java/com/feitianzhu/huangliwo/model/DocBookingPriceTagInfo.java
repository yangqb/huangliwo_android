package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.Map;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/25
 * time: 17:55
 * email: 694125155@qq.com
 */
public class DocBookingPriceTagInfo implements Serializable {
    public double basePrice;
    public double viewPrice;
    public double barePrice;
    public double packagePrice;
    public String productPackageCode;
    public Map<String, String> extMap;
}
