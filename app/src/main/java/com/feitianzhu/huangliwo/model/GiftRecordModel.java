package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

public class GiftRecordModel implements Serializable {
    public String createTime;// (string, optional): 创建时间 ,

    public int giftId;//(integer, optional):赠品id ,

    public String giftName;//(string, optional):赠品名称 ,

    public String merchantName;//(string, optional):商户名称 ,

    public double price;//(number, optional):赠品价格
}
