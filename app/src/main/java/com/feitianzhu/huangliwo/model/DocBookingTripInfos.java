package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/25
 * time: 17:15
 * email: 694125155@qq.com
 */
public class DocBookingTripInfos implements Serializable {
    public List<DocBookingTripItemsInfo> tripItems;
    public DocBookingTransferInfo transferInfo;
    public List<DocBookingClientBookingResultInfo> clientBookingResult;
    //public List<> baggageRuleInfos;
    public DocBookingTipsInfo tips;
}
