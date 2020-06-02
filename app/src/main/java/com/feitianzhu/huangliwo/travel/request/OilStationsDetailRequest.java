package com.feitianzhu.huangliwo.travel.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.core.network.BaseTravelRequest;
import com.feitianzhu.huangliwo.travel.bean.OilStationsDetailBean;

import java.util.List;

/**
 * Created by bch on 2020/5/20
 */
public class OilStationsDetailRequest extends BaseTravelRequest {
    public String gasIds;
    public String phone;
    public String accessToken;
    public String userId;
    @Override
    public String getAPIName() {
        return "fleetin/getOilStationsDetail";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder
                .append("gasIds", gasIds)
                .append("accessToken", accessToken)
                .append("userId", userId)
                .append("phone", phone));
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<OilStationsDetailBean>>() {
        };
    }
}
