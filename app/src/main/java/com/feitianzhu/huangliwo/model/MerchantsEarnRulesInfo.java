package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/17
 * time: 17:57
 * email: 694125155@qq.com
 */
public class MerchantsEarnRulesInfo implements Serializable {

    private List<MerchantsEarnRulesModel> list;

    public List<MerchantsEarnRulesModel> getList() {
        return list;
    }

    public void setList(List<MerchantsEarnRulesModel> list) {
        this.list = list;
    }

    public static class MerchantsEarnRulesModel implements Serializable {
        /**
         * status : 0
         * type : 2
         * phone : 13100680322
         * amount : 988
         * smName : 大闸蟹
         * nickName : null
         * createTime : 2020-01-15 19:27:25
         */

        private int status;
        private int type;
        private String phone;
        private double amount;
        private String smName;
        private String nickName;
        private String createTime;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getSmName() {
            return smName;
        }

        public void setSmName(String smName) {
            this.smName = smName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
