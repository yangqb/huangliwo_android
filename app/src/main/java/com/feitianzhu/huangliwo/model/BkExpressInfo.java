package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.Map;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/14
 * time: 14:31
 * email: 694125155@qq.com
 */
public class BkExpressInfo implements Serializable {
    public int id;
    public int price;
    public Map<String, String> invoiceType;
    public Map<String, String> receiverType;
}
