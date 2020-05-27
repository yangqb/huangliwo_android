package com.feitianzhu.huangliwo.shop.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;

/**
 * package name: com.feitianzhu.huangliwo.shop.request
 * user: yangqinbo
 * date: 2020/5/26
 * time: 19:29
 * email: 694125155@qq.com
 */
public class ExpressInfoRequest extends BaseRequest {
    public String userId;
    public String token;
    public String expressNum;
    public String expressName;
    public String orderNo;

    @Override
    public String getAPIName() {
        return "fhwl/order/commitExpress";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return builder.append("userId", userId)
                .append("accessToken", token)
                .append("orderNo", orderNo)
                .append("expressNum", expressNum)
                .append("expressName", expressName);
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<Boolean>() {
        };
    }
}
