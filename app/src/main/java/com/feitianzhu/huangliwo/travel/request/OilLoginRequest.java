package com.feitianzhu.huangliwo.travel.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.travel.base.OilTimeBaseRequest;

/**
 * Created by bch on 2020/5/20
 */
public class OilLoginRequest extends OilTimeBaseRequest {
    public String platformId;
    public String phone;

    @Override
    public String getAPIBaseURL() {
        return "https://test-mcs.czb365.com/";
    }

    @Override
    public String getAPIName() {
        return "services/v3/begin/platformLoginSimpleAppV4";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("platformId", platformId)
                .append("phone", phone)
        );
    }


    @Override
    public TypeReference getDatatype() {
        return new TypeReference<String>() {
        };
    }
}
