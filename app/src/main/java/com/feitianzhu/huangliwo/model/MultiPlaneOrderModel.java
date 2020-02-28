package com.feitianzhu.huangliwo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MultiPlaneOrderModel implements MultiItemEntity {
    public static final int GO_TYPE = 1;
    public static final int GO_COME_TYPE = 2;
    private int type;

    public MultiPlaneOrderModel(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
