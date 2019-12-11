package com.feitianzhu.fu700.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/9
 * time: 11:57
 * email: 694125155@qq.com
 */
public class MultiItemComment implements MultiItemEntity {
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

    public MultiItemComment(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
