package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.Map;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/25
 * time: 17:21
 * email: 694125155@qq.com
 */
public class DocBookingLxInvoiceInfo implements Serializable {
    public DocBookingInvoiceInfo invoiceInfo;
    public int defaultReceiver;
    public Map<Integer, String> receiverTypeMap;
}
