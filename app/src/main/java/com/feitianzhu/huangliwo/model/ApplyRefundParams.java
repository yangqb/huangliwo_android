package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/3
 * time: 21:10
 * email: 694125155@qq.com
 */
public class ApplyRefundParams implements Serializable {
    public String callbackUrl;//": "string",
    public String orderNo;//": "string",
    public String passengerIds;//": "string",
    public String refundCause;//": "string",
    public String refundCauseId;//": "string"
    public String amount;
}
