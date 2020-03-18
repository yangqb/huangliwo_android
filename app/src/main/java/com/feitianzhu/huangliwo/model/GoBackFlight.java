package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.plane
 * user: yangqinbo
 * date: 2020/3/17
 * time: 14:52
 * email: 694125155@qq.com
 */
public class GoBackFlight implements Serializable {
    public String mainCarrierShortName;//":"",
    public String carrier;//":"HU",
    public String carrierName;//":"海南航空",
    public String carrierFullName;//":"海南航空股份有限公司",
    public String flightCode;//":"HU7609",
    public int tof;//":"0",
    public int arf;//":"50",
    public boolean stops;//":"false",
    public String correct;//":"84%",
    public String lateTime;//":"16",
    public String distance;//":"1227",
    public String flightTimes;//":"2小时10分钟",
    public String depAirportCode;//":"PEK",
    public String arrAirportCode;//":"SHA",
    public String depAirport;//":"首都机场",
    public String arrAirport;//":"虹桥机场",
    public String depTerminal;//":"T1",
    public String arrTerminal;//":"T2",
    public String depTime;//":"15:50",
    public String arrTime;//":"18:00",
    public String pTrip;//":"",
    public String mainCarrier;//":"",
    public boolean codeShare;//":"false",
    public String flightTypeFullName;//":"波音738(中)",
    public String planeType;//":"738"

    /*@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GoBackFlight goBackFlight = (GoBackFlight) obj;
        return (this.depTime.equals(goBackFlight.depTime) && this.arrTime.equals(goBackFlight.arrTime) && this.price.equals(goBackFlight.price) && this.arrAirport.equals(goBackFlight.arrAirport) &&
                this.depAirport.equals(goBackFlight.depAirport) && this.flightCode.equals(goBackFlight.flightCode) && this.carrierName.equals(goBackFlight.carrierName));
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + depTime.hashCode();
        result = 31 * result + arrTime.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + arrAirport.hashCode();
        result = 31 * result + depAirport.hashCode();
        result = 31 * result + flightCode.hashCode();
        result = 31 * result + carrierName.hashCode();
        return result;
    }*/
}
