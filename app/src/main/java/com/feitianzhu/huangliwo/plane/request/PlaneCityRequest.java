package com.feitianzhu.huangliwo.plane.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.BaseTravelRequest;
import com.feitianzhu.huangliwo.core.network.BaseTravelUrlRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.model.CustomCityModel;
import com.feitianzhu.huangliwo.utils.Urls;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.plane.request
 * user: yangqinbo
 * date: 2020/6/4
 * time: 10:27
 * email: 694125155@qq.com
 */
public class PlaneCityRequest extends BaseTravelUrlRequest {
    public int flag; //1国内地点  2国际地点

    @Override
    public String getAPIName() {
        return "getPlace";
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return builder.append("flag", flag);
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<CustomCityModel>>() {

        };
    }
}
