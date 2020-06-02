package com.feitianzhu.huangliwo.im.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.BaseTravelRequest;
import com.feitianzhu.huangliwo.im.bean.ConverzServiceListBean;
import com.feitianzhu.huangliwo.utils.Urls;

import java.util.List;

public class ConverServiceRequest extends BaseTravelRequest {


    @Override
    public String getAPIName() {
        return "im/selectCS";
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<ConverzServiceListBean>>() {
        };
    }
}
