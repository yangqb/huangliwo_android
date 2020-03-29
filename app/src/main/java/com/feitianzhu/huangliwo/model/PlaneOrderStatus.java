package com.feitianzhu.huangliwo.model;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/27
 * time: 18:14
 * email: 694125155@qq.com
 */
public class PlaneOrderStatus {
    public static final int BOOK_OK = 0; //订单成功等待支付
    public static final int PAY_OK = 1;//支付成功等待出票
    public static final int TICKET_OK = 2;//出票完成
    public static final int TICKET_LOCK = 5;//出票中
    public static final int CANCEL_OK = 12;//订单取消
    public static final int WAIT_CONFIRM = 20;//等待座位确认
    public static final int APPLY_REFUNDMENT = 30;//退款待确认
    public static final int WAIT_REFUNDMENT = 31;//待退款
    public static final int REFUND_OK = 39;//退款完成
    public static final int APPLY_CHANGE = 40;//改签申请中
    public static final int CHANGE_OK = 42;//改签完成
    public static final int APPLY_RETURN_PAY = 50;//未出票申请退款
    public static final int ORDER_SUCESS_WAIT_PRICE_CONFIRM = 51;//定坐成功等待价格确认
    public static final int NONE = 100;
}
