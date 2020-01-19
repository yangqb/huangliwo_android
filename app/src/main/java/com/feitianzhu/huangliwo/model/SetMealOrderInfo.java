package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/16
 * time: 10:09
 * email: 694125155@qq.com
 */
public class SetMealOrderInfo implements Serializable {
    public static final int WAIT_PAY = 1;
    public static final int WAIT_USE = 2;
    public static final int REFUNDING = 3;
    public static final int REFUNDED = 4;
    public static final int CANCEL = 5;
    public static final int HAVE_USED = 6;

    //1 未支付，2 已支付(未使用)，3 退款中，4 已退款，5 订单取消(未支付的)6.已使用
    /**
     * all : [{"orderNo":"BM20200115183435634706","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:34:36","num":"153938705341","consumeTime":null,"payTime":null},{"orderNo":"BM20200115183506573936","status":2,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:35:06","num":"881238779148","consumeTime":null,"payTime":null},{"orderNo":"BM20200115183608251802","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:36:08","num":"072610132571","consumeTime":null,"payTime":null},{"orderNo":"BM20200115183709386661","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:37:09","num":"309118585416","consumeTime":null,"payTime":null},{"orderNo":"BM20200115192449882966","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 19:24:49","num":"086796913069","consumeTime":null,"payTime":null},{"orderNo":"BM20200115192725298419","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 19:27:25","num":"355257798387","consumeTime":null,"payTime":null},{"orderNo":"BM20200115202209825982","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 20:22:09","num":"081071119458","consumeTime":null,"payTime":null},{"orderNo":"BM20200115202638600272","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 20:26:38","num":"699633483672","consumeTime":null,"payTime":null}]
     * waitUseCount : 1
     * waitEval : [{"orderNo":"BM20200115183435634706","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:34:36","num":"153938705341","consumeTime":null,"payTime":null},{"orderNo":"BM20200115183506573936","status":2,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:35:06","num":"881238779148","consumeTime":null,"payTime":null},{"orderNo":"BM20200115183608251802","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:36:08","num":"072610132571","consumeTime":null,"payTime":null},{"orderNo":"BM20200115183709386661","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:37:09","num":"309118585416","consumeTime":null,"payTime":null},{"orderNo":"BM20200115192449882966","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 19:24:49","num":"086796913069","consumeTime":null,"payTime":null},{"orderNo":"BM20200115192725298419","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 19:27:25","num":"355257798387","consumeTime":null,"payTime":null},{"orderNo":"BM20200115202209825982","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 20:22:09","num":"081071119458","consumeTime":null,"payTime":null},{"orderNo":"BM20200115202638600272","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 20:26:38","num":"699633483672","consumeTime":null,"payTime":null}]
     * waitPayCount : 7
     * waitPay : [{"orderNo":"BM20200115183435634706","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:34:36","num":"153938705341","consumeTime":null,"payTime":null},{"orderNo":"BM20200115183608251802","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:36:08","num":"072610132571","consumeTime":null,"payTime":null},{"orderNo":"BM20200115183709386661","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:37:09","num":"309118585416","consumeTime":null,"payTime":null},{"orderNo":"BM20200115192449882966","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 19:24:49","num":"086796913069","consumeTime":null,"payTime":null},{"orderNo":"BM20200115192725298419","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 19:27:25","num":"355257798387","consumeTime":null,"payTime":null},{"orderNo":"BM20200115202209825982","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 20:22:09","num":"081071119458","consumeTime":null,"payTime":null},{"orderNo":"BM20200115202638600272","status":1,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 20:26:38","num":"699633483672","consumeTime":null,"payTime":null}]
     * waitUse : [{"orderNo":"BM20200115183506573936","status":2,"merchantName":"海底捞","smName":"大闸蟹","smImg":"http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png","isConsume":0,"amount":988,"isEval":0,"createTime":"2020-01-15 18:35:06","num":"881238779148","consumeTime":null,"payTime":null}]
     * waitEvalCount : 8
     */

    private int waitUseCount;
    private int waitPayCount;
    private int waitEvalCount;
    private int refundingCount;
    private List<SetMealOrderModel> all;
    private List<SetMealOrderModel> waitEval;
    private List<SetMealOrderModel> waitPay;
    private List<SetMealOrderModel> waitUse;
    private List<SetMealOrderModel> refunding;

    public int getRefundingCount() {
        return refundingCount;
    }

    public void setRefundingCount(int refundingCount) {
        this.refundingCount = refundingCount;
    }

    public List<SetMealOrderModel> getRefunding() {
        return refunding;
    }

    public void setRefunding(List<SetMealOrderModel> refunding) {
        this.refunding = refunding;
    }

    public int getWaitUseCount() {
        return waitUseCount;
    }

    public void setWaitUseCount(int waitUseCount) {
        this.waitUseCount = waitUseCount;
    }

    public int getWaitPayCount() {
        return waitPayCount;
    }

    public void setWaitPayCount(int waitPayCount) {
        this.waitPayCount = waitPayCount;
    }

    public int getWaitEvalCount() {
        return waitEvalCount;
    }

    public void setWaitEvalCount(int waitEvalCount) {
        this.waitEvalCount = waitEvalCount;
    }

    public List<SetMealOrderModel> getAll() {
        return all;
    }

    public void setAll(List<SetMealOrderModel> all) {
        this.all = all;
    }

    public List<SetMealOrderModel> getWaitEval() {
        return waitEval;
    }

    public void setWaitEval(List<SetMealOrderModel> waitEval) {
        this.waitEval = waitEval;
    }

    public List<SetMealOrderModel> getWaitPay() {
        return waitPay;
    }

    public void setWaitPay(List<SetMealOrderModel> waitPay) {
        this.waitPay = waitPay;
    }

    public List<SetMealOrderModel> getWaitUse() {
        return waitUse;
    }

    public void setWaitUse(List<SetMealOrderModel> waitUse) {
        this.waitUse = waitUse;
    }

    public static class SetMealOrderModel implements Serializable {
        /**
         * orderNo : BM20200115183506573936
         * status : 2//1 未支付，2 已支付(未使用)，3 退款中，4 已退款，5 订单取消(未支付的)6.已使用
         * merchantName : 海底捞
         * smName : 大闸蟹
         * smImg : http://39.106.65.35:8089/user/merchant/2020/01/14/52e5c69189fe4010807740e84adc9517.png,http://39.106.65.35:8089/user/merchant/2020/01/14/c1e508fd20934e03ab2661191370fae1.png,http://39.106.65.35:8089/user/merchant/2020/01/14/e5a0b34a1c2e407d9baf076304891b74.png,http://39.106.65.35:8089/user/merchant/2020/01/14/dc5dab16fac24bf5bd617582e2e07cba.png
         * isConsume : 0
         * amount : 988
         * isEval : 0
         * createTime : 2020-01-15 18:35:06
         * num : 881238779148
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

        public int getIsEval() {
            return isEval;
        }

        public void setIsEval(int isEval) {
            this.isEval = isEval;
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
}
