package com.feitianzhu.huangliwo.shop.model;

import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bch on 2020/5/18
 */
public class MerchantBean {
    //分类标题
    public String title;
    //优质
    public List<MerchantsModel> veryGood;
    //推荐
    public List<MerchantsModel> recommendFor;
    //热门
    public List<MerchantsModel> hot;




    public static ArrayList<String> getTitleList(List<MerchantBean> list) {
        ArrayList<String> strings = new ArrayList<>();
        for (MerchantBean shopClassifyBean : list) {
            strings.add(shopClassifyBean.title);
        }
        return strings;
    }
}
