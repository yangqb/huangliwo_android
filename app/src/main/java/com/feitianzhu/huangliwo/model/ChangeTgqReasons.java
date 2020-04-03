package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/3
 * time: 18:20
 * email: 694125155@qq.com
 */
public class ChangeTgqReasons implements Serializable {
    public int code;
    public String msg;
    public boolean will;
    public List<RefundPassengerPriceInfoListInfo> refundPassengerPriceInfoList; //改签查询此节点返回null，无需关注
    public List<FlightAlterSearchResultWithText> changeFlightSegmentList;
}
