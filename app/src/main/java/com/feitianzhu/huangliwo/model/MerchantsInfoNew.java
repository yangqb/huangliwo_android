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

public class MerchantsInfoNew implements Serializable {

    private List<MerchantsModel> veryGood;
    private List<MerchantsModel> recommendFor;
    private List<MerchantsModel> hot;

    public List<MerchantsModel> getVeryGood() {
        return veryGood;
    }

    public void setVeryGood(List<MerchantsModel> veryGood) {
        this.veryGood = veryGood;
    }

    public List<MerchantsModel> getRecommendFor() {
        return recommendFor;
    }

    public void setRecommendFor(List<MerchantsModel> recommendFor) {
        this.recommendFor = recommendFor;
    }

    public List<MerchantsModel> getHot() {
        return hot;
    }

    public void setHot(List<MerchantsModel> hot) {
        this.hot = hot;
    }

}
