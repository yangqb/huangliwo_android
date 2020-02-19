package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

public class VipGifListInfo implements Serializable {
    public List<VipGifModel> list;
    public double totalPrice;


    public static class VipGifModel implements Serializable {
        public String createTime;//":"2020-02-14T08:51:33.039Z",
        public String distance;//":"string",
        public int giftId;//":0,
        public String giftName;//":"string",
        public int isGet;//":0,
        public String latitude;//":"string",
        public String longitude;//":"string",
        public int merchantId;//":0,
        public String merchantName;//":"string",
        public double price;//":0
    }
}
