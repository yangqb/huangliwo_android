package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/15
 * time: 19:29
 * email: 694125155@qq.com
 */
public class SetMealOrderDetailInfo implements Serializable {


    /**
     * orderNo : BM20200115202638600272
     * status : 1
     * merchantName : 海底捞
     * smName : 大闸蟹
     * smImg : http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png
     * isConsume : 0
     * amount : 988
     * isEval : null
     * createTime : 2020-01-15 20:26:38
     * num : 699633483672
     * consumeTime : null
     * payTime : null
     */

    private String orderNo;
    private int status;
    private String merchantName;
    private String smName;
    private String smImg;
    private int isConsume;
    private double amount;
    private int isEval;
    private String createTime;
    private String num;
    private String consumeTime;
    private String payTime;
    private String remark;
    private int smId;
    private int merchantId;
    private String userId;
    public String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSmId() {
        return smId;
    }

    public void setSmId(int smId) {
        this.smId = smId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getSmName() {
        return smName;
    }

    public void setSmName(String smName) {
        this.smName = smName;
    }

    public String getSmImg() {
        return smImg;
    }

    public void setSmImg(String smImg) {
        this.smImg = smImg;
    }

    public int getIsConsume() {
        return isConsume;
    }

    public void setIsConsume(int isConsume) {
        this.isConsume = isConsume;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getIsEval() {
        return isEval;
    }

    public void setIsEval(int isEval) {
        this.isEval = isEval;
    }

    public String getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(String consumeTime) {
        this.consumeTime = consumeTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }
}
