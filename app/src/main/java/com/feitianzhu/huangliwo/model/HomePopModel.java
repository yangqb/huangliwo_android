package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/8
 * time: 18:17
 * email: 694125155@qq.com
 */
public class HomePopModel implements Serializable {

    /**
     * popup : {"title":"新年活动弹窗","imgUrl":"http://39.106.65.35:8089|/images/1/files/goods/tPopup/2020/01/1%20(2).jpg|/images/1/files/goods/tPopup/2020/01/1%20(2).jpg","status":1,"sequence":1,"likeType":4}
     */

    private PopupBean popup;

    public PopupBean getPopup() {
        return popup;
    }

    public void setPopup(PopupBean popup) {
        this.popup = popup;
    }

    public static class PopupBean {
        /**
         * title : 新年活动弹窗
         * imgUrl : http://39.106.65.35:8089|/images/1/files/goods/tPopup/2020/01/1%20(2).jpg|/images/1/files/goods/tPopup/2020/01/1%20(2).jpg
         * status : 1
         * sequence : 1
         * likeType : 4
         */

        private String title;
        private String imgUrl;
        private int status;
        private int sequence;
        private int likeType;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }

        public int getLikeType() {
            return likeType;
        }

        public void setLikeType(int likeType) {
            this.likeType = likeType;
        }
    }
}
