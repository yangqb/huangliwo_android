package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/18
 * time: 16:30
 * email: 694125155@qq.com
 */
public class WithdrawRecordInfo implements Serializable {

    private List<WithdrawRecordModel> list;

    public List<WithdrawRecordModel> getList() {
        return list;
    }

    public void setList(List<WithdrawRecordModel> list) {
        this.list = list;
    }

    public static class WithdrawRecordModel implements Serializable {
        /**
         * orderNo : W20200118161531203658
         * userId : 156785
         * merchantId : null
         * userName : 黄鹂窝0322
         * amount : 666
         * feeRate : null
         * rnAmount : null
         * bankCardId : null
         * status : 0
         * applyDate : 2020-01-18 16:15:31
         * type : 2
         * examineBy : null
         * examineDate : null
         * refuseReason : null
         * aliAccount : 13100680321
         * isTrans : 0
         * paymentImg : null
         */

        private String orderNo;
        private int userId;
        private int merchantId;
        private String userName;
        private double amount;
        private String feeRate;
        private double rnAmount;
        private String bankCardId;
        private int status;
        private String applyDate;
        private int type;
        private String examineBy;
        private String examineDate;
        private String refuseReason;
        private String aliAccount;
        private int isTrans;
        private String paymentImg;

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

        public int getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getFeeRate() {
            return feeRate;
        }

        public void setFeeRate(String feeRate) {
            this.feeRate = feeRate;
        }

        public double getRnAmount() {
            return rnAmount;
        }

        public void setRnAmount(double rnAmount) {
            this.rnAmount = rnAmount;
        }

        public String getBankCardId() {
            return bankCardId;
        }

        public void setBankCardId(String bankCardId) {
            this.bankCardId = bankCardId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getExamineBy() {
            return examineBy;
        }

        public void setExamineBy(String examineBy) {
            this.examineBy = examineBy;
        }

        public String getExamineDate() {
            return examineDate;
        }

        public void setExamineDate(String examineDate) {
            this.examineDate = examineDate;
        }

        public String getRefuseReason() {
            return refuseReason;
        }

        public void setRefuseReason(String refuseReason) {
            this.refuseReason = refuseReason;
        }

        public String getAliAccount() {
            return aliAccount;
        }

        public void setAliAccount(String aliAccount) {
            this.aliAccount = aliAccount;
        }

        public int getIsTrans() {
            return isTrans;
        }

        public void setIsTrans(int isTrans) {
            this.isTrans = isTrans;
        }

        public String getPaymentImg() {
            return paymentImg;
        }

        public void setPaymentImg(String paymentImg) {
            this.paymentImg = paymentImg;
        }
    }
}
