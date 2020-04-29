package com.feitianzhu.huangliwo.pushshop.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/4/29
 * time: 11:14
 * email: 694125155@qq.com
 */
public class MultiShopFront implements MultiItemEntity {
    public static final int upImg = 1;
    public static final int LookImg = 2;
    private int type;
    private Integer id;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MultiShopFront(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
