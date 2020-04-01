package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/30
 * time: 17:35
 * email: 694125155@qq.com
 */
public class InterOrderDetailInfo implements Serializable {
    public String createTime;
    public int orderStatus;
    public String orderNo;
    public InterContactInfo contactInfo;
    public List<InterOrderDetailPayInfo> payInfo;
    public InterOrderDetailFlightInfo flightInfo;
    public InterOrderDetailPassengerInfo passengerInfo;
    public InterOrderDetailXcdInfo xcdInfo;
    public InterOrderDetailExtInfo extInfo;
    public List<Object> serviceInfo;
    public List<InterOrderDetailProductInfo> productInfo;
}
