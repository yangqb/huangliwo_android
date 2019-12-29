package com.feitianzhu.fu700.model;

import java.io.Serializable;
import java.util.List;

public class ShopClassify {

    private List<GGoodsClsListBean> gGoodsClsList;
    private List<BaseGoodsListBean> goodsList;

    public List<BaseGoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<BaseGoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }

    public List<GGoodsClsListBean> getGGoodsClsList() {
        return gGoodsClsList;
    }

    public void setGGoodsClsList(List<GGoodsClsListBean> gGoodsClsList) {
        this.gGoodsClsList = gGoodsClsList;
    }

    public static class GGoodsClsListBean implements Serializable {
        /**
         * clsId : 1
         * clsName : 美食
         * clsImg : http://e.hiphotos.baidu.com/image/pic/item/a1ec08fa513d2697e542494057fbb2fb4316d81e.jpg
         * childrenList : []
         */

        private int clsId;
        private String clsName;
        private String clsImg;

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
    }
}
