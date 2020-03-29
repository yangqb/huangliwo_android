package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/25
 * time: 17:23
 * email: 694125155@qq.com
 */
public class DocBookingInvoiceInfo implements Serializable {
    public String receiptType;
    public int receiptCode;
    public boolean supportElectronicInvoice;
    public List<DocBookingInvoiceTypeListInfo> invoiceTypeList;
}
