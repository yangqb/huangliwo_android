package com.feitianzhu.huangliwo.financial.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * package name: com.feitianzhu.huangliwo.financial.bean
 * user: yangqinbo
 * date: 2020/4/2
 * time: 16:44
 * email: 694125155@qq.com
 */
public class MultiFinancialInfo implements MultiItemEntity {
    public static final int All_FINANCIAL = 1;
    public static final int MY_FINANCIAL = 2;
    private int type;

    public MultiFinancialInfo(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
