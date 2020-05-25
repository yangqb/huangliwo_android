package com.feitianzhu.huangliwo.pushshop.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.feitianzhu.huangliwo.model.MerchantGiftOrderModel;
import com.feitianzhu.huangliwo.model.MerchantsEarnRulesInfo;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/1/3
 * time: 17:57
 * email: 694125155@qq.com
 */
public class SelfMerchantsModel implements MultiItemEntity {
    public static final int RULES_TYPE = 1;
    public static final int ORDER_TYPE = 2;
    public static final int GIFT_ORDER_TYPE = 3;
    private int type;

    private MerchantsEarnRulesInfo.MerchantsEarnRulesModel merchantsEarnRulesModel;
    private MerchantGiftOrderModel merchantGiftOrderModel;

    public MerchantGiftOrderModel getMerchantGiftOrderModel() {
        return merchantGiftOrderModel;
    }

    public void setMerchantGiftOrderModel(MerchantGiftOrderModel merchantGiftOrderModel) {
        this.merchantGiftOrderModel = merchantGiftOrderModel;
    }

    public MerchantsEarnRulesInfo.MerchantsEarnRulesModel getMerchantsEarnRulesModel() {
        return merchantsEarnRulesModel;
    }

    public void setMerchantsEarnRulesModel(MerchantsEarnRulesInfo.MerchantsEarnRulesModel merchantsEarnRulesModel) {
        this.merchantsEarnRulesModel = merchantsEarnRulesModel;
    }

    public SelfMerchantsModel(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
