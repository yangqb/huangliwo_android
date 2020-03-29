package com.feitianzhu.huangliwo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/11
 * time: 19:42
 * email: 694125155@qq.com
 */
public class MultiPriceInfo implements MultiItemEntity, Serializable {
    public static final int DOMESTIC_TYPE = 1;
    public static final int INTERNATIONAL_TYPE = 2;
    public static final int DOMESTIC_GO_BACK_TYPE = 3;
    private int type;
    public VenDorsInfo venDorsInfo;
    public InternationalPriceInfo internationalPriceInfo;
    public GoBackVendors goBackVendors;

    public MultiPriceInfo(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
