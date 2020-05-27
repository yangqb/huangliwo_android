package com.feitianzhu.huangliwo.shop.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;

/**
 * package name: com.feitianzhu.huangliwo.shop.request
 * user: yangqinbo
 * date: 2020/5/27
 * time: 9:20
 * email: 694125155@qq.com
 */
public class ApplyReturnGoodsRequest extends BaseRequest {
    public String userId;
    public String token;
    public String orderNo;
    public String reason;
    public String status;
    public String imgs;

    @Override
    public String getAPIName() {
        return "fhwl/order/refundOrder";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return builder.append("userId", userId)
                .append("accessToken", token)
                .append("orderNo", orderNo)
                .append("reason", reason)
                .append("status", status)
                .append("imgs", imgs);
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<Boolean>() {
        };
    }
}
