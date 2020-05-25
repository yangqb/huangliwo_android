package com.feitianzhu.huangliwo.shop.model;

import com.feitianzhu.huangliwo.model.BaseGoodsListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bch on 2020/5/18
 */
public class ShopClassifyBean {
    //分类标题
    public String title;
    //优质
    public List<BaseGoodsListBean> boutique;
    //推荐
    public List<BaseGoodsListBean> recommendFor;
    //热门
    public List<BaseGoodsListBean> hot;

    public static ArrayList<String> getTitleList(List<ShopClassifyBean> list) {
        ArrayList<String> strings = new ArrayList<>();
        for (ShopClassifyBean shopClassifyBean : list) {
            strings.add(shopClassifyBean.title);
        }
        return strings;
    }
}
