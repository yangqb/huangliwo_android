package com.feitianzhu.huangliwo.strategy.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseTravelRequest;
import com.feitianzhu.huangliwo.strategy.bean.TitileBean;

import java.util.ArrayList;
import java.util.List;


public class TitleIdRequest extends BaseTravelRequest {
    @Override
    public String getAPIName() {
        return "title/columnlist";
    }


    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<ArrayList<TitileBean>>() {
        };
    }
}
