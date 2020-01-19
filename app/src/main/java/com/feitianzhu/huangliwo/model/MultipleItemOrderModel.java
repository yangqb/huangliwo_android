package com.feitianzhu.huangliwo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/16
 * time: 10:27
 * email: 694125155@qq.com
 */
public class MultipleItemOrderModel implements MultiItemEntity {
    public static final int SETMEAL_ORDER = 1;
    public static final int GOODS_ORDER = 2;
    private int type;

    public MultipleItemOrderModel(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    private GoodsOrderInfo.GoodsOrderListBean goodsOrderListBean;
    private SetMealOrderInfo.SetMealOrderModel setMealOrderModel;

    public GoodsOrderInfo.GoodsOrderListBean getGoodsOrderListBean() {
        return goodsOrderListBean;
    }

    public void setGoodsOrderListBean(GoodsOrderInfo.GoodsOrderListBean goodsOrderListBean) {
        this.goodsOrderListBean = goodsOrderListBean;
    }

    public SetMealOrderInfo.SetMealOrderModel getSetMealOrderModel() {
        return setMealOrderModel;
    }

    public void setSetMealOrderModel(SetMealOrderInfo.SetMealOrderModel setMealOrderModel) {
        this.setMealOrderModel = setMealOrderModel;
    }
}
