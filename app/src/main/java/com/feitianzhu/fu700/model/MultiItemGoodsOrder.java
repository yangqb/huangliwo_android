package com.feitianzhu.fu700.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/16
 * time: 9:45
 * email: 694125155@qq.com
 */
public class MultiItemGoodsOrder implements MultiItemEntity {
    /*
     * 1 未支付，2 待发货，3 待收货，4 已完成（已收货），5 退款中，6 已退款，7 订单取消（未支付的）
     * */
    public static final int TYPE_NO_PAY = 1;
    public static final int TYPE_WAIT_DELIVERY = 2;
    public static final int TYPE_WAIT_RECEIVING = 3;
    public static final int TYPE_COMPLETED = 4;
    public static final int TYPE_REFUND = 5;
    public static final int TYPE_REFUNDED = 6;
    public static final int TYPE_CANCEL = 7;
    private int type;

    public MultiItemGoodsOrder(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public GoodsOrderInfo.GoodsOrderListBean goodsOrderListBean;

    public GoodsOrderInfo.GoodsOrderListBean getGoodsOrderListBean() {
        return goodsOrderListBean;
    }

    public void setGoodsOrderListBean(GoodsOrderInfo.GoodsOrderListBean goodsOrderListBean) {
        this.goodsOrderListBean = goodsOrderListBean;
    }
}
