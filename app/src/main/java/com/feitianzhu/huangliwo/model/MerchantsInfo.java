package com.feitianzhu.huangliwo.model;

import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/15
 * time: 15:23
 * email: 694125155@qq.com
 */
public class MerchantsInfo implements Serializable {
    private List<MerchantsModel> list;

    public List<MerchantsModel> getList() {
        return list;
    }

    public void setList(List<MerchantsModel> list) {
        this.list = list;
    }
}
