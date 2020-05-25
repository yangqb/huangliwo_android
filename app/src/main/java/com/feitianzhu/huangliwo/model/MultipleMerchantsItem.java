package com.feitianzhu.huangliwo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.feitianzhu.huangliwo.pushshop.bean.SetMealInfo;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/15
 * time: 16:14
 * email: 694125155@qq.com
 */
public class MultipleMerchantsItem implements MultiItemEntity {
    public static final int SETMEAL_TYPE = 1;
    public static final int COMMENTS_TYPE = 2;
    public static final int GIFT_TYPE = 3;
    private int type;

    private SetMealInfo setMealInfo;
    private SetMealEvalDetailInfo.SetMealEvalDetailModel evalDetailModel;
    private VipGifListInfo.VipGifModel gifModel;

    public VipGifListInfo.VipGifModel getGifModel() {
        return gifModel;
    }

    public void setGifModel(VipGifListInfo.VipGifModel gifModel) {
        this.gifModel = gifModel;
    }

    public SetMealEvalDetailInfo.SetMealEvalDetailModel getEvalDetailModel() {
        return evalDetailModel;
    }

    public void setEvalDetailModel(SetMealEvalDetailInfo.SetMealEvalDetailModel evalDetailModel) {
        this.evalDetailModel = evalDetailModel;
    }

    public SetMealInfo getSetMealInfo() {
        return setMealInfo;
    }

    public void setSetMealInfo(SetMealInfo setMealInfo) {
        this.setMealInfo = setMealInfo;
    }

    public MultipleMerchantsItem(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
