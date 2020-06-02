package com.feitianzhu.huangliwo.strategy.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.strategy.bean.TitileBean;

import java.util.List;

import static com.feitianzhu.huangliwo.utils.Urls.TICKET_BASE_URL;

public class TitleIdRequest extends BaseRequest {
    @Override
    public String getAPIName() {
        return "title/columnlist";
    }

    @Override
    public String getAPIBaseURL() {
        return TICKET_BASE_URL;
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<List<TitileBean>>() {
        };
    }
}
