package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/14
 * time: 14:19
 * email: 694125155@qq.com
 */
public class BookingInfo implements Serializable {
    public String bookingStatus;
    public String errMsg;
    public String bookingTag;
    public List<BkFlightInfo> flightInfo;
    public BkPriceInfo priceInfo;
    public BkExtInfo extInfo;
    public BkTgqShowData tgqShowData;
    public BkTgqShowData childTgqData;
    public String ticketTime;
    //bookingIns //开放平台暂不支持保险，忽略
    public BkExpressInfo expressInfo;
    public BkPolicyInfo policyInfo;
    public List<BkBaggageRuleInfo> baggageRuleInfos;
    public List<BkSpecialProductRule> specialProductRule;
    public int ret;
}
