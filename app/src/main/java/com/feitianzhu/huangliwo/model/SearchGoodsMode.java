package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/26
 * time: 9:26
 * email: 694125155@qq.com
 */
public class SearchGoodsMode implements Serializable {
    private List<BaseGoodsListBean> list;

    public List<BaseGoodsListBean> getList() {
        return list;
    }

    public void setList(List<BaseGoodsListBean> list) {
        this.list = list;
    }
}
