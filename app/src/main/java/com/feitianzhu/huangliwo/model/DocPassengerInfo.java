package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/25
 * time: 16:55
 * email: 694125155@qq.com
 */
public class DocPassengerInfo implements Serializable {
    public String name;
    public int ageType;
    public int sex;
    public String birthday;
    public String cardType;
    public String cardNo;
    public String mobilePreNum;
    public String mobile;
    public List<String> priceTags;
}
