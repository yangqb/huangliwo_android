package com.feitianzhu.fu700.home.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.feitianzhu.fu700.model.BaseGoodsListBean;

/**
 * @class name：com.feitianzhu.fu700.home.entity
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/29 0029 下午 1:48
 */
public class ShopAndMerchants implements MultiItemEntity {
    public static final int TYPE_SERIES = 1;
    public static final int TYPE_PESALE = 2;
    private int itemType;

    public ShopAndMerchants(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public HomeEntity.RecommendListBean recommendListBean;
    public BaseGoodsListBean shopsList;

    public HomeEntity.RecommendListBean getRecommendListBean() {
        return recommendListBean;
    }

    public void setRecommendListBean(HomeEntity.RecommendListBean recommendListBean) {
        this.recommendListBean = recommendListBean;
    }

    public BaseGoodsListBean getShopsList() {
        return shopsList;
    }

    public void setShopsList(BaseGoodsListBean shopsList) {
        this.shopsList = shopsList;
    }
}
