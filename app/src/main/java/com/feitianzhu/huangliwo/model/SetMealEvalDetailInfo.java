package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/16
 * time: 19:07
 * email: 694125155@qq.com
 */
public class SetMealEvalDetailInfo implements Serializable {

    private List<SetMealEvalDetailModel> list;

    public List<SetMealEvalDetailModel> getList() {
        return list;
    }

    public void setList(List<SetMealEvalDetailModel> list) {
        this.list = list;
    }

    public static class SetMealEvalDetailModel implements Serializable {
        /**
         * evalId : 4
         * merchantId : 30
         * userId : 849501
         * content : 大概好久那你呢吃VB不别扭
         * star : null
         * evalDate : 1579171746000
         * isShow : 1
         * imgs : http://39.106.65.35:8089/user/merchant/2020/01/16/95667096f0b742cea0f9018233ee7e25.png,http://39.106.65.35:8089/user/merchant/2020/01/16/7903a71c85e743938a15254175d4df91.png
         * orderNo : BM20200115183435634706
         * nickName : 黄鹂窝0322
         * headImg : http://39.106.65.35:8089/user/default_headimg.png
         * smContent : 大闸蟹
         */

        private int evalId;
        private int merchantId;
        private int userId;
        private String content;
        private String star;
        private String evalDate;
        private int isShow;
        private String imgs;
        private String orderNo;
        private String nickName;
        private String headImg;
        private String smContent;

        public int getEvalId() {
            return evalId;
        }

        public void setEvalId(int evalId) {
            this.evalId = evalId;
        }

        public int getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getEvalDate() {
            return evalDate;
        }

        public void setEvalDate(String evalDate) {
            this.evalDate = evalDate;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getSmContent() {
            return smContent;
        }

        public void setSmContent(String smContent) {
            this.smContent = smContent;
        }
    }
}
