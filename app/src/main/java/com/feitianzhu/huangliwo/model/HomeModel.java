package com.feitianzhu.huangliwo.model;

import com.feitianzhu.huangliwo.home.entity.HomeEntity;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/5/8
 * time: 16:08
 * email: 694125155@qq.com
 */
public class HomeModel implements Serializable {
    public List<HomeEntity.BannerListBean> bannerList;
    public List<MerchantsModel> merchantList;//推广商户 3个
    public List<BaseGoodsListBean> goodsListfove;//热门商品 4个 ,
    public List<BaseGoodsListBean> goodsList;//推广商品 ,
    public String activityImg;//活动推广图 ,
    public HotGood hotGood;//便利爆品图 ,
    public List<GoodClsImgsModel> goodClsImgs; //更多优惠图 ,
}
