package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/4
 * time: 18:17
 * email: 694125155@qq.com
 */
public class ItineraryInfo implements Serializable {
    public int code;
    public String message;
    public String orderNo;
    public int expressFee;
    //public Object receiptInfo;
    public ReimburseTypeVoInfo reimburseTypeVo;
}
