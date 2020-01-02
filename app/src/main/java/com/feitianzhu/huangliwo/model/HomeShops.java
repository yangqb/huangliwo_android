package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/25
 * time: 18:24
 * email: 694125155@qq.com
 */
public class HomeShops implements Serializable {
    private List<BaseGoodsListBean> goodsList;

    public List<BaseGoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<BaseGoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }
}
