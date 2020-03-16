package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/14
 * time: 14:26
 * email: 694125155@qq.com
 */
public class BkFlightInfo implements Serializable {
    public String dptCity;
    public String arrCity;
    public String dpt;
    public String arr;
    public String dptAirport;
    public String arrAirport;
    public String dptTerminal;
    public String arrTerminal;
    public String dptTime;
    public String arrTime;
    public String dptDate;
    public String arrDate;
    public String flightNum;
    public String carrierName;
    public String cabin;
    public String carrier;
    public int stops;
    public List<StopInfo> stopInfoList;
    public boolean codeShare;
    public String childCabin;
    public String babyCabin;
    public int tof;
    public int arf;
    public int ctof;
    public String actFlightNum;
    public String cbcn;
    public String ccbcn;
    public String bcbcn;
    public String stopAirport;
    public String stopCity;
    public String actCarrier;
    public String actCarrierName;
    public String actPlaneType;
    public String planeCode;
    public String flightTimes;
}
