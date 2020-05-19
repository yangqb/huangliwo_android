package com.feitianzhu.huangliwo.home.request;

import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.model.HomeShops;
import com.lzy.okgo.model.HttpHeaders;
import com.vector.update_app.UpdateAppBean;
import com.alibaba.fastjson.TypeReference;

public class GoodsListRequest extends BaseRequest {
    public String token;
    public String userId;
    public int pageNo = 1;

      public GoodsListRequest(String token, String userId, int pageNo) {
        this.token = token;
        this.userId = userId;
        this.pageNo = pageNo;

    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public String getAPIName() {
        return "fhwl/index/pageGoods";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return builder.append("accessToken", token).append("userId", userId).append("limitNum", Constant.PAGE_SIZE)
                .append("curPage", pageNo + "");

    }

    @Override
    public HttpHeaders addHeads(HttpHeaders headers) {
        return headers;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<HomeShops>() {
        };    }
}
