package com.feitianzhu.huangliwo.vip.bean;

import android.support.annotation.DrawableRes;

/**
 * Created by bch on 2020/5/26
 */
public class VipBean {
    private String title;
    @DrawableRes
    private int id;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public VipBean(String title, int id) {
        this.title = title;
        this.id = id;
    }
}
