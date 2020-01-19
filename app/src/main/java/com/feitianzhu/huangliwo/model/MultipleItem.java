package com.feitianzhu.huangliwo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;

/**
 * @class name：com.feitianzhu.fu700.model
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/20 0020 下午 2:44
 */
public class MultipleItem implements MultiItemEntity {
    public static final int MERCHANTS = 1;
    public static final int GOODS = 2;
    private int itemType;

    private BaseGoodsListBean goodsListBean;
    private MerchantsModel merchantsModel;

    public MerchantsModel getMerchantsModel() {
        return merchantsModel;
    }

    public void setMerchantsModel(MerchantsModel merchantsModel) {
        this.merchantsModel = merchantsModel;
    }

    public BaseGoodsListBean getGoodsListBean() {
        return goodsListBean;
    }

    public void setGoodsListBean(BaseGoodsListBean goodsListBean) {
        this.goodsListBean = goodsListBean;
    }

    public MultipleItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

}
