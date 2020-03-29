package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/25
 * time: 17:13
 * email: 694125155@qq.com
 */
public class DocResultBookingInfo implements Serializable {
    public int type;
    public List<DocBookingTripInfos> tripInfos;
    public DocBookingPassengerContact passengerContact;
    public DocBookingExpressInfo expressInfo;
    public List<DocBookingCardTypeListInfo> cardTypeList;
    public String bookingTag;
}
