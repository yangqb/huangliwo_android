package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/30
 * time: 16:55
 * email: 694125155@qq.com
 */
public class DocOrderDetailInfo implements Serializable {
    public long expiresDate;//":1585895495000,
    public long nowTimeStamp;//":1590719796249
    public String message;
    public DocOrderDetail detail;
    public DocOrderDetailXcdInfo xcd;
    public DocOrderDetailOtherInfo other;
    public DocOrderDetailContacterInfo contacterInfo;
    public List<DocOrderDetailPassengersInfo> passengers;
    public List<DocOrderDetailFlightInfo> flightInfo;
    public List<DocOrderDetailPassengerTypesInfo> passengerTypes;
}
