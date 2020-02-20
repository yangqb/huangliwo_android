package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/17
 * time: 15:41
 * email: 694125155@qq.com
 */
public class ShoppingCartModel implements Serializable {

    public double allMoney;
    public List<CartGoodsModel> SCcar;
    public List<CartGoodsModel> allSCcar;


    public static class CartGoodsModel implements Serializable {
        public int carId;
        public int goodsId;
        public int userId;
        public int goodsCount;
        public String speci;
        public String title;
        public double price;
        public double rebatePv;
        public String goodsImg;
        public double totalMoney;
        public double totalRebatePv;
        public double postage;
        public int checks;
        public String speciName;
    }

}
