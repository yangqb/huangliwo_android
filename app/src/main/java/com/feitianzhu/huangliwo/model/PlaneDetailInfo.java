package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/5
 * time: 19:56
 * email: 694125155@qq.com
 */
public class PlaneDetailInfo implements Serializable {
    public String date;
    public String depCode;
    public String depAirport;
    public String depTerminal;
    public String btime;
    public String arrCode;
    public String arrAirport;
    public String arrTerminal;
    public String etime;
    public String carrier;
    public String com;
    public String code;
    public boolean meal;
    public boolean zhiji;
    public String correct;
    public boolean stop;
    public int stopsNum;
    public String stopCityCode;
    public String stopCityName;
    public String stopAirportCode;
    public String stopAirportName;
    public String stopAirportFullName;
    //public String stopInfoList
    public boolean codeShare;
    public String actCode;
    public int arf;
    public int tof;
    public int distance;
    public String flightType;
    public boolean officeTicket;
    public String bairdrome;
    public String eairdrome;
    public String flightTimes;
    public List<VenDorsInfo> vendors;

}
