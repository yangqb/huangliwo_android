package com.feitianzhu.fu700.model;

public class BaseGoodsListBean {
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
    private int price;
    private int rebatePv;
    private String summary;

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

}
