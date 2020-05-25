package com.feitianzhu.huangliwo.core.network.test;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.model.HomeShops;

import static com.feitianzhu.huangliwo.common.Constant.UAPDATE;

/**
 * Created by bch on 2020/5/11
 */
public class UpdataRequest extends BaseRequest {
    public String accessToken = "";
    public String userId = "";


    @Override
    public String getAPIName() {
        return UAPDATE;
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("accessToken", accessToken)
                .append("userId", userId)
                .append("type", "1"));
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<HomeShops>() {
        };
    }


    //    http://8.129.218.83:8088/fhwl/soft/newv?accessToken=5fde8fdbb42c406b96d06b9a7c3e86e1&userId=321276&type=1


}
