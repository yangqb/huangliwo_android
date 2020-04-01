package com.feitianzhu.huangliwo.model;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/31
 * time: 10:41
 * email: 694125155@qq.com
 */
public class PlaneOrderTableEntity implements CustomTabEntity {

    public String title;

    public PlaneOrderTableEntity(String title) {
        this.title = title;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return 0;
    }

    @Override
    public int getTabUnselectedIcon() {
        return 0;
    }
}
