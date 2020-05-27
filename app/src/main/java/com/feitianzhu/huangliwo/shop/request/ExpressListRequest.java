package com.feitianzhu.huangliwo.shop.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.shop.model.ExpressModel;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.shop.request
 * user: yangqinbo
 * date: 2020/5/27
 * time: 14:35
 * email: 694125155@qq.com
 */
public class ExpressListRequest extends BaseRequest {
    public String userId;
    public String token;

    @Override
    public String getAPIName() {
        return "fhwl/express/getExpressList";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return builder.append("userId", userId)
                .append("accessToken", token);
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<ExpressModel>>() {

        };
    }
}
