package com.feitianzhu.huangliwo.travel.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.core.network.BaseTravelUrlRequest;
import com.feitianzhu.huangliwo.travel.bean.OilOrederBean;

import java.util.List;

public class OilOrderUrlRequest extends BaseTravelUrlRequest {
    public int limitNum;
    public int curPage;
    public String phone;
    public String accessToken;
    public String userId;

    @Override
    public String getAPIName() {
        return "fleetin/getOrderInfo";
    }

    public OilOrderUrlRequest(int limitNum, int curPage, String phone) {
        this.limitNum = limitNum;
        this.curPage = curPage;
        this.phone = phone;
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder
                .append("limitNum",limitNum)
                .append("accessToken",accessToken)
                .append("userId",userId)
                .append("curPage",curPage).append("phone",phone));
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<OilOrederBean>>(){};
    }
}
