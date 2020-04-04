package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/4
 * time: 18:23
 * email: 694125155@qq.com
 */
public class RefundServiceInfo implements Serializable {
    public int code;
    public String message;
    public String createTime;
    public boolean canApply;
    public String orderNo;
    public String invoiceType;
    public int invoiceCode;
    public int expressFee;
}
