package com.feitianzhu.huangliwo.pushshop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/9
 * time: 16:55
 * email: 694125155@qq.com
 */
public class MerchantsClassifyModel implements Serializable {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * clsId : 1
         * clsName : 美食
         * clsImg : http://192.168.0.21:8089/images/1/images/goods/shopGoodsCls/2019/12/1%20(2).jpg
         * sequence : 1
         */

        private int clsId;
        private String clsName;
        private String clsImg;
        private int sequence;

        public int getClsId() {
            return clsId;
        }

        public void setClsId(int clsId) {
            this.clsId = clsId;
        }

        public String getClsName() {
            return clsName;
        }

        public void setClsName(String clsName) {
            this.clsName = clsName;
        }

        public String getClsImg() {
            return clsImg;
        }

        public void setClsImg(String clsImg) {
            this.clsImg = clsImg;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }
    }
}
