package com.feitianzhu.huangliwo.model;

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
    private double rebatePv;
    private String summary;
    private String goodsIntroduceImg; //长图
    private String sales;
    private String stockCount;
    private double postage;
    private String isExtend;
    private String connectPhone;
    private String isPoints;
    private int isCollect;
    private List<GoodsImgsListBean> goodsImgsList;
    private List<GoodsEvaluateMode> evalList;

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public List<GoodsEvaluateMode> getEvalList() {
        return evalList;
    }

    public void setEvalList(List<GoodsEvaluateMode> evalList) {
        this.evalList = evalList;
    }

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

    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
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

    public double getRebatePv() {
        return rebatePv;
    }

    public void setRebatePv(double rebatePv) {
        this.rebatePv = rebatePv;
    }

    public static class GoodsImgsListBean implements Serializable {
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

    public static class GoodsEvaluateMode implements Serializable {

        /**
         * evalId : 1
         * content : 嗯www
         * evalDate : Dec 21, 2019 7:26:34 PM
         * nickName : HLW0321
         * headImg : http://39.106.65.35:8089/user/headImg/2019/12/13/ffc06cf20697433d88131ee1114d4baa.jpg
         * evalImgs : http://127.0.0.1:8089/good/evaluate/2019/12/21/3e4c783f7753431586089acefc8e259b.png,http://127.0.0.1:8089/good/evaluate/2019/12/21/8152c02fabb94217a052171f1872e77f.png
         */

        private int evalId;
        private String content;
        private String evalDateStr;
        private String nickName;
        private String headImg;
        private String evalImgs;
        private String norms;
        private String goodsName;
        private String evalDate;

        public String getEvalDate() {
            return evalDate;
        }

        public void setEvalDate(String evalDate) {
            this.evalDate = evalDate;
        }

        public String getEvalDateStr() {
            return evalDateStr;
        }

        public void setEvalDateStr(String evalDateStr) {
            this.evalDateStr = evalDateStr;
        }

        public String getNorms() {
            return norms;
        }

        public void setNorms(String norms) {
            this.norms = norms;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public int getEvalId() {
            return evalId;
        }

        public void setEvalId(int evalId) {
            this.evalId = evalId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getEvalImgs() {
            return evalImgs;
        }

        public void setEvalImgs(String evalImgs) {
            this.evalImgs = evalImgs;
        }
    }
}
