package com.feitianzhu.fu700.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @class name：com.feitianzhu.fu700.model
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/20 0020 下午 2:44
 */
public class MultipleItem implements MultiItemEntity {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    private int itemType;

    public BaseGoodsListBean goodsListBean;

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
