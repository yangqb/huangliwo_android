package com.feitianzhu.fu700.model;

import java.io.Serializable;
import java.util.List;

public class BaseGoodsListBean implements Serializable {
    /**
     * goodsId : 4
     * goodsName : 测试数据3
     * title : 测试数据3
     * goodsImg : http://127.0.0.1/images/1/images/content/2019/11/timg(1).jpg
     * price : 1000
     * rebatePv : 100
     * goodsImgsList : []
     * summary 商品说明
     */

    private int goodsId;
    private String goodsName;
    private String title;
    private String goodsImg;
    private double price;
    private int rebatePv;
    private String summary;
    private String goodsIntroduceImg; //长图
    private String sales;
    private String stockCount;
    private String postage;
    private String isExtend;
    private String connectPhone;
    private String isPoints;
    private List<GoodsImgsListBean> goodsImgsList;

    public String getGoodsIntroduceImg() {
        return goodsIntroduceImg;
    }

    public void setGoodsIntroduceImg(String goodsIntroduceImg) {
        this.goodsIntroduceImg = goodsIntroduceImg;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getStockCount() {
        return stockCount;
    }

    public void setStockCount(String stockCount) {
        this.stockCount = stockCount;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
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

    public String getIsPoints() {
        return isPoints;
    }

    public void setIsPoints(String isPoints) {
        this.isPoints = isPoints;
    }

    public List<GoodsImgsListBean> getGoodsImgsList() {
        return goodsImgsList;
    }

    public void setGoodsImgsList(List<GoodsImgsListBean> goodsImgsList) {
        this.goodsImgsList = goodsImgsList;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
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

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRebatePv() {
        return rebatePv;
    }

    public void setRebatePv(int rebatePv) {
        this.rebatePv = rebatePv;
    }

    public static class GoodsImgsListBean implements Serializable{
        /**
         * imgId : 1
         * goodsId : 2
         * goodsImg : 2
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
