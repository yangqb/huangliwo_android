package com.feitianzhu.huangliwo.travel.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.travel.base.BaseTravelRequest;

/**
 * Created by bch on 2020/5/20
 */
public abstract class OilTimeBaseRequest extends BaseRequest {

    @Override
    public String getAPIBaseURL() {
        return "http://test-mcs.czb365.com/";
    }


    @Override
    public TypeReference getResponseType() {
        return new TypeReference<OilTimeResponse>() {
        };
    }

}
