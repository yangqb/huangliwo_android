package com.feitianzhu.huangliwo.shop.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;

public class GoodsDetailRequest extends BaseRequest {
    public String goodsId;

    @Override
    public String getAPIName() {
        return "fhwl/index/getGoodsDetail";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return builder.append("goodsId", goodsId);
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<BaseGoodsListBean>() {
        };
    }

    @Override
    public boolean usePost() {
        return false;
    }
}
