package com.feitianzhu.huangliwo.model;

import com.feitianzhu.huangliwo.utils.PayUtils;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/12
 * time: 18:47
 * email: 694125155@qq.com
 */
public class GoodsOrderInfo implements Serializable {
    /*
     * 1 未支付，2 待发货，3 待收货(已发货)，4 已完成（已收货），5 退款中，6 已退款，7 订单取消（未支付的）9退货申请中，10同意退货申请 11待商家收货12待商家退款，13退货完成
     * */
    public static final int TYPE_NO_PAY = 1;
    public static final int TYPE_WAIT_DELIVERY = 2;
    public static final int TYPE_WAIT_RECEIVING = 3;
    public static final int TYPE_COMPLETED = 4;
    public static final int TYPE_REFUND = 5;
    public static final int TYPE_REFUNDED = 6;
    public static final int TYPE_CANCEL = 7;
    public static final int TYPE_All = -1;
    public static final int TYPE_WAIT_COMMENTS = 0;
    public static final int TYPE_REFUNDING_GOODS = 9;
    public static final int TYPE_AGREE_REFUND_GOODS = 10;
    public static final int TYPE_WAIT_MERCHANT_RECEIVING = 11;
    public static final int TYPE_WAIT_MERCHANT_REFUND = 12;
    public static final int TYPE_COMPLETED_REFUND_GOODS = 13;

    private List<GoodsOrderListBean> goodsOrderList;

    public List<GoodsOrderListBean> getGoodsOrderList() {
        return goodsOrderList;
    }

    public void setGoodsOrderList(List<GoodsOrderListBean> goodsOrderList) {
        this.goodsOrderList = goodsOrderList;
    }

    public static class GoodsOrderListBean implements Serializable {
     /*
     *      amount (number, optional): 总价格 ,
attributeVal (string, optional): 商品属性 ,
count (integer, optional): 数量 ,
createDate (string, optional): 下单时间 ,
expiresDate (integer, optional): 过期时间 ,
goodName (string, optional): 商品名称 ,
goodsImg (string, optional): 商品图片 ,
nowTimeStamp (integer, optional): 当前时间戳 ,
orderNo (string, optional): 订单编号 ,
price (number, optional): 单价 ,
shopAddress (收货地址, optional): 订单收货地址 ,
shopName (string, optional): 店铺名称 ,
status (integer, optional): 订单状态 1 未支付，2 待发货，3 待收货(已发货)，4 已完成（已收货），5 退款中，6 已退款，7 订单取消（未支付的） ,
valueId (integer, optional): 商品属性id
}收货地址 {
addressId (integer, optional): addressId ,
areaId (string, optional): 区id ,
areaName (string, optional): 区名称 ,
cityId (string, optional): 市id ,
cityName (string, optional): 市名称 ,
detailAddress (string, optional): 详细地址 ,
isDefalt (integer, optional): 是否是默认地址 0否 1是 ,
phone (string, optional): 电话号码 ,
provinceId (string, optional): 省id ,
provinceName (string, optional): 省名称 ,
userId (integer, optional): 用户id ,
userName (string, optional): 用户名
     * */

        private String attributeVal;
        private String orderNo;
        private int userId;
        private double amount;
        private double postage;
        private double rebatePv;
        private String channel;
        private String createDate;
        private long expiresDate;
        private long nowTimeStamp;
        private String addressId;
        private int status;
        private String goodName;
        private String goodsImg;
        private String valueId;
        private int count;//订单详情页和列表页的商品数量
        private String shopName;
        private double price;
        private int goodId;
        private int goodsQty; //下单时候传给后台的商品数量
        private String logisticCpName; //快递公司
        private String expressNo;//快递单号
        private String detailAddress;//":"内蒙古自治区包头市白云鄂博矿区啊图形我要呀我在真学在真学在真",
        private String buyerName;//":"HLW0321",
        private String buyerPhone;//":"13100680321",
        private int isEval;//是否评价 1.已评价
        private String remark;//备注
        private String connectPhone;//客服电话
        private String refuseReason;//拒绝退款原因
        private int isVipOrder;//1是399,0是商品
        private String supplierName;
        private String supplierPhone;
        private String supplierAddress;
        private String refundImg;
        private String returnReason; //拒绝退货原因
        private String refundExpressNum;
        private String refundExpressCom;
        private String refundExpressCode;

        public String getRefundExpressCode() {
            return refundExpressCode;
        }

        public void setRefundExpressCode(String refundExpressCode) {
            this.refundExpressCode = refundExpressCode;
        }

        public String getRefundExpressNum() {
            return refundExpressNum;
        }

        public void setRefundExpressNum(String refundExpressNum) {
            this.refundExpressNum = refundExpressNum;
        }

        public String getRefundExpressCom() {
            return refundExpressCom;
        }

        public void setRefundExpressCom(String refundExpressCom) {
            this.refundExpressCom = refundExpressCom;
        }

        public String getReturnReason() {
            return returnReason;
        }

        public void setReturnReason(String returnReason) {
            this.returnReason = returnReason;
        }

        public String getRefundImg() {
            return refundImg;
        }

        public void setRefundImg(String refundImg) {
            this.refundImg = refundImg;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public String getSupplierPhone() {
            return supplierPhone;
        }

        public void setSupplierPhone(String supplierPhone) {
            this.supplierPhone = supplierPhone;
        }

        public String getSupplierAddress() {
            return supplierAddress;
        }

        public void setSupplierAddress(String supplierAddress) {
            this.supplierAddress = supplierAddress;
        }

        public int getIsVipOrder() {
            return isVipOrder;
        }

        public void setIsVipOrder(int isVipOrder) {
            this.isVipOrder = isVipOrder;
        }

        public String getRefuseReason() {
            return refuseReason;
        }

        public void setRefuseReason(String refuseReason) {
            this.refuseReason = refuseReason;
        }

        public String getConnectPhone() {
            return connectPhone;
        }

        public void setConnectPhone(String connectPhone) {
            this.connectPhone = connectPhone;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getIsEval() {
            return isEval;
        }

        public void setIsEval(int isEval) {
            this.isEval = isEval;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public String getBuyerPhone() {
            return buyerPhone;
        }

        public void setBuyerPhone(String buyerPhone) {
            this.buyerPhone = buyerPhone;
        }

        public String getLogisticCpName() {
            return logisticCpName;
        }

        public void setLogisticCpName(String logisticCpName) {
            this.logisticCpName = logisticCpName;
        }

        public String getExpressNo() {
            return expressNo;
        }

        public void setExpressNo(String expressNo) {
            this.expressNo = expressNo;
        }

        public int getGoodsQty() {
            return goodsQty;
        }

        public void setGoodsQty(int goodsQty) {
            this.goodsQty = goodsQty;
        }

        public long getExpiresDate() {
            return expiresDate;
        }

        public void setExpiresDate(long expiresDate) {
            this.expiresDate = expiresDate;
        }

        public long getNowTimeStamp() {
            return nowTimeStamp;
        }

        public void setNowTimeStamp(long nowTimeStamp) {
            this.nowTimeStamp = nowTimeStamp;
        }

        public String getAttributeVal() {
            return attributeVal;
        }

        public void setAttributeVal(String attributeVal) {
            this.attributeVal = attributeVal;
        }

        public String getGoodName() {
            return goodName;
        }

        public void setGoodName(String goodName) {
            this.goodName = goodName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getGoodId() {
            return goodId;
        }

        public void setGoodId(int goodId) {
            this.goodId = goodId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getPostage() {
            return postage;
        }

        public void setPostage(double postage) {
            this.postage = postage;
        }

        public double getRebatePv() {
            return rebatePv;
        }

        public void setRebatePv(double rebatePv) {
            this.rebatePv = rebatePv;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public String getValueId() {
            return valueId;
        }

        public void setValueId(String valueId) {
            this.valueId = valueId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

    }
}
