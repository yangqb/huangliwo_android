package com.feitianzhu.fu700.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/9
 * time: 17:36
 * email: 694125155@qq.com
 */
public class ProductParameters implements Serializable {

    private List<GoodsSpecifications> goodslist;

    public List<GoodsSpecifications> getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(List<GoodsSpecifications> goodslist) {
        this.goodslist = goodslist;
    }

    public static class GoodsSpecifications implements Serializable {
        /**
         * attributeId : 3
         * attributeName : 颜色
         * skuValueList : [{"valueId":5,"goodsId":10,"attributeVal":"黑色"},{"valueId":6,"goodsId":10,"attributeVal":"白色"},{"valueId":7,"goodsId":10,"attributeVal":"红色"},{"valueId":8,"goodsId":10,"attributeVal":"蓝色"},{"valueId":9,"goodsId":10,"attributeVal":"紫色"}]
         */
        private int attributeId;
        private String attributeName;
        private List<GoodsSpecifications.SkuValueListBean> skuValueList;

        public int getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(int attributeId) {
            this.attributeId = attributeId;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public List<SkuValueListBean> getSkuValueList() {
            return skuValueList;
        }

        public void setSkuValueList(List<SkuValueListBean> skuValueList) {
            this.skuValueList = skuValueList;
        }

        public static class SkuValueListBean implements Serializable {
            /**
             * valueId : 5
             * goodsId : 10
             * attributeVal : 黑色
             */

            private boolean isSelect;
            private int valueId;
            private int goodsId;
            private String attributeVal;

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }

            public int getValueId() {
                return valueId;
            }

            public void setValueId(int valueId) {
                this.valueId = valueId;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public String getAttributeVal() {
                return attributeVal;
            }

            public void setAttributeVal(String attributeVal) {
                this.attributeVal = attributeVal;
            }
        }

    }
}
