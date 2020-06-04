package com.feitianzhu.huangliwo.im.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseTravelUrlRequest;
import com.feitianzhu.huangliwo.im.bean.ConverzServiceListBean;

import java.util.List;

public class ConverServiceUrlRequest extends BaseTravelUrlRequest {


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
