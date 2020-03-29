package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/19
 * time: 16:31
 * email: 694125155@qq.com
 */
public class CustomPlaneDetailInfo implements Serializable {
    public PlaneDetailInfo customDocGoFlightInfo; //国内单程信息
    public VenDorsInfo customDocGoPriceInfo; //国内单程报价
    public GoBackFlightList customDocGoBackFlightInfo; //国内往返信息
    public GoBackVendors customDocGoBackPriceInfo; //国内往返报价

    public SearchInternationalFlightModel customInterFlightInfo;//国外单程和往返信息
    public InternationalPriceInfo customInterPriceInfo;//国外单程和往返报价

    public String goDate;
    public String backDate;
    public String goCityName;
    public String backCityName;
    public String depCode;
    public String arrCode;
}
