package com.feitianzhu.fu700.model;

import java.io.Serializable;
import java.util.Date;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/5
 * time: 10:01
 * email: 694125155@qq.com
 */
public class GoodsOrderModel implements Serializable {
    private int userId;
    /**
     * 金额
     */
    private double amount;
    /**
     * 邮费
     */
    private double postage;
    private double rebatePv;
    /**
     * 订单类型(1:购买订单，2：退货单)
     */
    private int type;
    /**
     * 支付渠道支付渠道（wx：微信，alipay：支付宝，balance：余额)
     */
    private String channel;
    /**
     * 三方支付单号
     */
    private String thirdOrderNo;
    private String payProof;//线下支付凭证  /**
    private Date createDate;   // 下单时间 */
    /**
     * 支付时间
     */
    private Date payDate;
    /**
     * 发货时间
     */
    private Date deliveryDate;
    /**
     * 收货时间
     */
    private Date receiptDate;
    /**
     * 收货地址ID
     */
    private int addressId;
    private String remark;
    /**
     * -1：已退货，0:未支付，1：已支付，2：已发货，3：已收货、4：已完成
     */
    private int status;
    private String isPoints;
    private String isExtend;
    private String logisticsNo;
    private String logisticsCode;
    private String receiptName;
    private String receiptPhone;
    private String regionName;
    private String detailAddr; //收货地址
    private String leaveMsg;
    private String buyerName;   //购买人姓名
    private String buyerPhone;  // 购买人电话
    private String buyerEmail;  // 买单人邮箱
    private int parentId;   // 推荐人ID
    private String parentName;   // 推荐人姓名
    private String parentPhone;  // 推荐人电话
    private String parentEmail;  //付款账号
    private String payAccount;   // 推荐人邮箱*/
    private String isPay;//支付结果1：成功0：失败（取消支付，待支付，支付失败）
    private String orderNo; //订单号

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getThirdOrderNo() {
        return thirdOrderNo;
    }

    public void setThirdOrderNo(String thirdOrderNo) {
        this.thirdOrderNo = thirdOrderNo;
    }

    public String getPayProof() {
        return payProof;
    }

    public void setPayProof(String payProof) {
        this.payProof = payProof;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsPoints() {
        return isPoints;
    }

    public void setIsPoints(String isPoints) {
        this.isPoints = isPoints;
    }

    public String getIsExtend() {
        return isExtend;
    }

    public void setIsExtend(String isExtend) {
        this.isExtend = isExtend;
    }

    public String getLogisticsNo() {
        return logisticsNo;
    }

    public void setLogisticsNo(String logisticsNo) {
        this.logisticsNo = logisticsNo;
    }

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr;
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
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

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }
}
