package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/27
 * time: 17:52
 * email: 694125155@qq.com
 */
public class PlaneOrderInfo implements Serializable {
    public List<PlaneOrderModel> all;
    public List<PlaneOrderModel> refundOrUpdateList;
    public List<PlaneOrderModel> waiPayList;
    public List<PlaneOrderModel> waitTicketedList;
    public List<PlaneOrderModel> ticketedList;
    public int waitTicketedCount;
    public int ticketedCount;
    public int refundOrUpdateCount;
    public int waiPayCount;
}
