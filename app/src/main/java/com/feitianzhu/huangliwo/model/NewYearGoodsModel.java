package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/7
 * time: 14:30
 * email: 694125155@qq.com
 */
public class NewYearGoodsModel implements Serializable {
    private List<BaseGoodsListBean> activityList;

    public List<BaseGoodsListBean> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<BaseGoodsListBean> activityList) {
        this.activityList = activityList;
    }
}
