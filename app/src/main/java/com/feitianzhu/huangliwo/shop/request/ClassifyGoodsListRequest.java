package com.feitianzhu.huangliwo.shop.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.shop.model.ShopClassifyBean;

import java.util.List;


/**
 * Created by bch on 2020/5/18
 */
public class ClassifyGoodsListRequest extends BaseRequest {
    public String accessToken;
    public String userId;
    public String clsId;

    @Override
    public String getAPIName() {
        return "/fhwl/shop/getGoodsList";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("accessToken", accessToken)
                .append("userId", userId)
                .append("clsId", clsId));
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<ShopClassifyBean>>() {
        };
    }
}
