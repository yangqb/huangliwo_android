package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

public class CollectionInfo implements Serializable {
    public List<CollectionModel> collectList;

    public static class CollectionModel implements Serializable {
        public String collectId;// (integer, optional): 收藏物主键ID ,
        public String createDate;// (string, optional): 商品或商家收藏时间 ,
        public String goodsImg;// (string, optional): 商品图或商家图 ,
        public String goodsName;// (string, optional): 商品名称或店铺地址 ,
        public int idValue;// (integer, optional): 商品或商家收藏物ID ,
        public double price;// (number, optional): 商品价格 ,
        public double rebatePv;// (number, optional): 商品让利或商家折扣 ,
        public String title;// (string, optional): 商品或商家标题 ,
        public int type;// (integer, optional): 收藏类型1：收藏商户，2：收藏商品 ,
        public int userId;// (integer, optional): 用户ID
        public int sellOut;
    }

}
