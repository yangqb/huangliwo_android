package com.feitianzhu.huangliwo.travel.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.travel.base.BaseTravelRequest;
import com.feitianzhu.huangliwo.travel.model.OilStationsDetailBean;
import com.feitianzhu.huangliwo.utils.StringUtils;

import java.util.List;

/**
 * Created by bch on 2020/5/20
 */
public class OilStationsDetailRequest extends BaseTravelRequest {
    public String gasIds;
    public String phone;

    @Override
    public String getAPIName() {
        return "fleetin/getOilStationsDetail";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("gasIds", gasIds)
                .append("phone", phone));
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<OilStationsDetailBean>>() {
        };
    }
}
