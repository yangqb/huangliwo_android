package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/24
 * time: 18:36
 * email: 694125155@qq.com
 */
public class UserGoodVo implements Serializable {
    private List<ReslutBean> reslut;

    public List<ReslutBean> getReslut() {
        return reslut;
    }

    public void setReslut(List<ReslutBean> reslut) {
        this.reslut = reslut;
    }

    public static class ReslutBean implements Serializable {
        /**
         * icon : http://127.0.0.1:8089/user/headImg/2019/12/22/14dfebfd0ab244b8a5a454460e99b2a8.jpg
         * phone : 13697754467
         * nickname : 何盛
         * buyDate : 2019-12-24 11:36:37.0
         * amount : 0.01
         */
           /* amount;// (number, optional): 消费金额 ,
    buyDate;// (string, optional): 消费时间 ,
    icon;// (string, optional): 头像 ,
    nickname;// (string, optional): 昵称 ,
    phone;// (string, optional): 手机号码*/

        private String icon;
        private String phone;
        private String nickname;
        private String buyDate;
        private double amount;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getBuyDate() {
            return buyDate;
        }

        public void setBuyDate(String buyDate) {
            this.buyDate = buyDate;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}
