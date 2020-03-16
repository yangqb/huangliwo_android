package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/4
 * time: 20:37
 * email: 694125155@qq.com
 */
public class SearchFlightModel implements Serializable {
    public int total;
    public List<FlightModel> flightInfos;

    public static class FlightModel implements Serializable {
        public String dpt;
        public String arr;
        public String dptTerminal;
        public String arrAirport;
        public String arrTerminal;
        public String dptTime;
        public String arrTime;
        public String flightNum;
        public double barePrice;
        public String tag;
        public double discount;
        public String carrier;
        public String cabin;
        public String distance;
        public String flightTimes;
        public String planetype;
        public String flightTypeFullName;
        public String actFlightNum;
        public boolean codeShare;
        public int stopsNum;
        public String cabinCount;
        public boolean meal;
        public int minVppr;
        public String bfTag;
        public double bfBarePrice;
        public double bfPrice;
        public double price;
        public String stopAirportFullName;
        public String stopAirportName;
        public String stopAirportCode;
        public String stopCityName;
        public String stopCityCode;
        public String dptAirport;
        public double childPrice;
        public double zk;
    }

}
