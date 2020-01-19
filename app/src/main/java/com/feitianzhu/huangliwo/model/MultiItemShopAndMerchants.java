package com.feitianzhu.huangliwo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsClassifyModel;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/15
 * time: 10:08
 * email: 694125155@qq.com
 */
public class MultiItemShopAndMerchants implements MultiItemEntity {
    public static final int SHOP_TYPE = 1;
    public static final int MERCHANTS_TYPE = 2;
    private int type;

    public MultiItemShopAndMerchants(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    private ShopClassify.GGoodsClsListBean shopClassifyModel;
    private MerchantsClassifyModel.ListBean merchantsClassifyModel;

    public ShopClassify.GGoodsClsListBean getShopClassifyModel() {
        return shopClassifyModel;
    }

    public void setShopClassifyModel(ShopClassify.GGoodsClsListBean shopClassifyModel) {
        this.shopClassifyModel = shopClassifyModel;
    }

    public MerchantsClassifyModel.ListBean getMerchantsClassifyModel() {
        return merchantsClassifyModel;
    }

    public void setMerchantsClassifyModel(MerchantsClassifyModel.ListBean merchantsClassifyModel) {
        this.merchantsClassifyModel = merchantsClassifyModel;
    }
}
