package com.feitianzhu.huangliwo.pushshop.bean;

import com.feitianzhu.huangliwo.model.VipGifListInfo;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/1/14
 * time: 15:55
 * email: 694125155@qq.com
 */
public class SetMealListInfo implements Serializable {
    private List<SetMealInfo> list;
    private List<VipGifListInfo.VipGifModel> merchantGiftList;

    public List<VipGifListInfo.VipGifModel> getMerchantGiftList() {
        return merchantGiftList;
    }

    public void setMerchantGiftList(List<VipGifListInfo.VipGifModel> merchantGiftList) {
        this.merchantGiftList = merchantGiftList;
    }

    public List<SetMealInfo> getList() {
        return list;
    }

    public void setList(List<SetMealInfo> list) {
        this.list = list;
    }
}
