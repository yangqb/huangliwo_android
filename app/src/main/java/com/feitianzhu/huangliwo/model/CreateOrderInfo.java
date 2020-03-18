package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/16
 * time: 17:01
 * email: 694125155@qq.com
 */
public class CreateOrderInfo implements Serializable {

    public int id;//":0,
    public String noPayAmount;//:2350,
    public String orderNo;//202032212684",//合单订单号
    public int status;//0,
    public String validateLink;//":null,
    public boolean isSplicing;//":true,
    public List<SubOrdersInfo> subOrders;//
}
