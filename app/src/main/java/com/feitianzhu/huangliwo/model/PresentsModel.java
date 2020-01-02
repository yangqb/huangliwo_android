package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/30
 * time: 20:02
 * email: 694125155@qq.com
 */
public class PresentsModel implements Serializable {

    private List<ShopGiftListBean> shopGiftList;

    public List<ShopGiftListBean> getShopGiftList() {
        return shopGiftList;
    }

    public void setShopGiftList(List<ShopGiftListBean> shopGiftList) {
        this.shopGiftList = shopGiftList;
    }

    public static class ShopGiftListBean {
        /**
         * giftId : 5
         * giftImg : http://39.106.65.35:8089/images/1/images/goods/shopGift/2019/12/f01_06liping1%402x.png
         * giftName : 伊山瑾睡莲水莹修护面膜
         * giftTitle : 伊山瑾睡莲水莹修护面膜
         * giftExhibition : 1
         * giftExplain : 伊山瑾睡莲水莹修护面膜
         */

        private int giftId;
        private String giftImg;
        private String giftName;
        private String giftTitle;
        private int giftExhibition;
        private String giftExplain;

        public int getGiftId() {
            return giftId;
        }

        public void setGiftId(int giftId) {
            this.giftId = giftId;
        }

        public String getGiftImg() {
            return giftImg;
        }

        public void setGiftImg(String giftImg) {
            this.giftImg = giftImg;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }

        public String getGiftTitle() {
            return giftTitle;
        }

        public void setGiftTitle(String giftTitle) {
            this.giftTitle = giftTitle;
        }

        public int getGiftExhibition() {
            return giftExhibition;
        }

        public void setGiftExhibition(int giftExhibition) {
            this.giftExhibition = giftExhibition;
        }

        public String getGiftExplain() {
            return giftExplain;
        }

        public void setGiftExplain(String giftExplain) {
            this.giftExplain = giftExplain;
        }
    }
}
