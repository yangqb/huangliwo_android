package com.feitianzhu.huangliwo.pushshop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/1/10
 * time: 15:29
 * email: 694125155@qq.com
 */
public class SelfMerchantsListInfo implements Serializable {


    private List<MerchantsModel> list;

    public List<MerchantsModel> getList() {
        return list;
    }

    public void setList(List<MerchantsModel> list) {
        this.list = list;
    }

}
