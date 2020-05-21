package com.feitianzhu.huangliwo.shop.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.shop.model.MerchantBean;

import java.util.List;


/**
 * Created by bch on 2020/5/18
 */
public class ShopDetailRequest extends BaseRequest {
    public String goodsId;

    @Override
    public String getAPIName() {
        return "fhwl/index/getGoodsDetail";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("goodsId", goodsId));
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<BaseGoodsListBean>() {
        };
    }
}
