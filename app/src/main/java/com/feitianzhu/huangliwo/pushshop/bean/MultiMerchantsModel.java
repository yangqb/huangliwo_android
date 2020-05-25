package com.feitianzhu.huangliwo.pushshop.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/3/18
 * time: 13:16
 * email: 694125155@qq.com
 */
public class MultiMerchantsModel implements MultiItemEntity, Serializable {
    public static final int EXAMINE_TYPE = 0;
    public static final int PASSED_TYPE = 1;
    public static final int UNPASSED_TYPE = 2;
    public int type;
    public MerchantsModel merchants;

    public MultiMerchantsModel(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
