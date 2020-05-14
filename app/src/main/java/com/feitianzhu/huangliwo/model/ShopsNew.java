package com.feitianzhu.huangliwo.model;

import java.util.List;

/**
 * Created by bch on 2020/5/13
 */
public class ShopsNew {

    private List<BaseGoodsListBean> boutique;
    private List<BaseGoodsListBean> recommendFor;
    private List<BaseGoodsListBean> hot;

    public List<BaseGoodsListBean> getBoutique() {
        return boutique;
    }

    public void setBoutique(List<BaseGoodsListBean> boutique) {
        this.boutique = boutique;
    }

    public List<BaseGoodsListBean> getRecommendFor() {
        return recommendFor;
    }

    public void setRecommendFor(List<BaseGoodsListBean> recommendFor) {
        this.recommendFor = recommendFor;
    }

    public List<BaseGoodsListBean> getHot() {
        return hot;
    }

    public void setHot(List<BaseGoodsListBean> hot) {
        this.hot = hot;
    }

    public static class HotBean {
        /**
         * goodsId : 438
         * clsId : null
         * goodsName : 金边透明玻璃大号沙拉碗
         * title : 金边透明玻璃大号沙拉碗
         * summary : 金边透明玻璃大号沙拉碗创意个性家用水果碗日式餐具
         * goodsIntroduceImg :
         * goodsIntroduceImgList : null
         * goodsImg : http://182.92.177.234/images/9e0bb45d1f2844c4a8c0ce31b8dbc661/images/goods/2020/300/356/1(1).jpg
         * price : 29
         * rebatePv : 6
         * sales : 1688
         * details : null
         * stockCount : 2051
         * speci : null
         * postage : 0
         * isShelf : null
         * isExtend : 0
         * createDate : null
         * createBy : null
         * updateBy : null
         * updateDate : null
         * goodsImgsList : [{"imgId":6961,"goodsId":438,"goodsImg":"http://182.92.177.234/images/9e0bb45d1f2844c4a8c0ce31b8dbc661/images/goods/2020/300/356/4(1).jpg"},{"imgId":6958,"goodsId":438,"goodsImg":"http://182.92.177.234/images/9e0bb45d1f2844c4a8c0ce31b8dbc661/images/goods/2020/300/356/1(1).jpg"},{"imgId":6959,"goodsId":438,"goodsImg":"http://182.92.177.234/images/9e0bb45d1f2844c4a8c0ce31b8dbc661/images/goods/2020/300/356/2(1).jpg"},{"imgId":6960,"goodsId":438,"goodsImg":"http://182.92.177.234/images/9e0bb45d1f2844c4a8c0ce31b8dbc661/images/goods/2020/300/356/3(1).jpg"}]
         * evalList : []
         * collectId : null
         * connectPhone :
         * priceOrderBy : null
         * isPoints : null
         * isCollect : null
         * stockCountReduceNum : null
         * isActivity : null
         * type : 1
         * sequence : 1
         * sskuValueList : null
         */

        private int goodsId;
        private String clsId;
        private String goodsName;
        private String title;
        private String summary;
        private String goodsIntroduceImg;
        private String goodsImg;
        private int price;
        private int rebatePv;
        private int sales;
        private int stockCount;
        private int postage;
        private String isExtend;
        private String connectPhone;
        private int type;
        private int sequence;
        private List<GoodsImgsListBean> goodsImgsList;

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public Object getClsId() {
            return clsId;
        }

        public void setClsId(String clsId) {
            this.clsId = clsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getGoodsIntroduceImg() {
            return goodsIntroduceImg;
        }

        public void setGoodsIntroduceImg(String goodsIntroduceImg) {
            this.goodsIntroduceImg = goodsIntroduceImg;
        }


        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getRebatePv() {
            return rebatePv;
        }

        public void setRebatePv(int rebatePv) {
            this.rebatePv = rebatePv;
        }

        public int getSales() {
            return sales;
        }

        public void setSales(int sales) {
            this.sales = sales;
        }


        public int getStockCount() {
            return stockCount;
        }

        public void setStockCount(int stockCount) {
            this.stockCount = stockCount;
        }


        public int getPostage() {
            return postage;
        }

        public void setPostage(int postage) {
            this.postage = postage;
        }


        public String getIsExtend() {
            return isExtend;
        }

        public void setIsExtend(String isExtend) {
            this.isExtend = isExtend;
        }


        public String getConnectPhone() {
            return connectPhone;
        }

        public void setConnectPhone(String connectPhone) {
            this.connectPhone = connectPhone;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getSequence() {
            return sequence;
        }

        public void setSequence(int sequence) {
            this.sequence = sequence;
        }


        public List<GoodsImgsListBean> getGoodsImgsList() {
            return goodsImgsList;
        }

        public void setGoodsImgsList(List<GoodsImgsListBean> goodsImgsList) {
            this.goodsImgsList = goodsImgsList;
        }

        public static class GoodsImgsListBean {
            /**
             * imgId : 6961
             * goodsId : 438
             * goodsImg : http://182.92.177.234/images/9e0bb45d1f2844c4a8c0ce31b8dbc661/images/goods/2020/300/356/4(1).jpg
             */

            private int imgId;
            private int goodsId;
            private String goodsImg;

            public int getImgId() {
                return imgId;
            }

            public void setImgId(int imgId) {
                this.imgId = imgId;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsImg() {
                return goodsImg;
            }

            public void setGoodsImg(String goodsImg) {
                this.goodsImg = goodsImg;
            }
        }
    }
}

