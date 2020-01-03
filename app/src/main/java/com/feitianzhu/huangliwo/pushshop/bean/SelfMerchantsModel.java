package com.feitianzhu.huangliwo.pushshop.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/1/3
 * time: 17:57
 * email: 694125155@qq.com
 */
public class SelfMerchantsModel implements MultiItemEntity {
    public static final int OFFLINE_TYPE = 1;
    public static final int ONLINE_TYPE = 2;
    private int type;
    private String orderInfo;

    public SelfMerchantsModel(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}
