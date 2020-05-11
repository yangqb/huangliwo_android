package com.feitianzhu.huangliwo.core.network;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.utils.Urls;

/**
 * Created by bch on 2020/5/11
 */
public abstract class BaseRequest extends BaseApiRequest {
    @Override
    public String getAPIBaseURL() {
        return Urls.BASE_URL;
    }

    @Override
    public TypeReference getResponseType() {
        return new TypeReference<BaseResponse>() {
        };
    }
}

