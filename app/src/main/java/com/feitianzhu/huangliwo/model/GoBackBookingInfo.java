package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/25
 * time: 15:25
 * email: 694125155@qq.com
 */
public class GoBackBookingInfo implements Serializable {
    public int flightType;
    public String orderFrom;
    public String bookingTag;
    public int soloChild; //是否允许单独购买儿童票单独购买儿童票时传1，其他传0

}
