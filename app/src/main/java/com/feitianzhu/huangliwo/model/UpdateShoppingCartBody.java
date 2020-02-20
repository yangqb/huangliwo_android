package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

public class UpdateShoppingCartBody implements Serializable {
    public int checks;
    public int goodsCount;
    public int carId;
    public String speci; // 商品规格
    public String speciName;// 规格名称
}
