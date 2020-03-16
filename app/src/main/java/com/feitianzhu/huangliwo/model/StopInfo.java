package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/9
 * time: 16:44
 * email: 694125155@qq.com
 */
public class StopInfo implements Serializable {
    public String airportCode;
    public String airportName;
    public String cityCode;
    public String cityName;
    public String arrDate;
    public String arrTime;
    public String depDate;
    public String depTime;
    public String stopTime;
    public int crossDays;
    public String countryName;
}
