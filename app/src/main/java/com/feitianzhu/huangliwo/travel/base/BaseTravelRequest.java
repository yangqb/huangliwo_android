package com.feitianzhu.huangliwo.travel.base;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.BaseResponse;
import com.feitianzhu.huangliwo.utils.Urls;

/**
 * Created by bch on 2020/5/20
 */
public abstract class BaseTravelRequest extends BaseRequest {
    /**
     * 设置域名 端口
     *
     * @return
     */
    @Override
    public String getAPIBaseURL() {
        return "http://192.168.0.142:8087/";
    }

    /**
     * 设置base返回基类,进行统一处理
     *
     * @return
     */
    @Override
    public TypeReference getResponseType() {
        return new TypeReference<BaseResponse>() {
        };
    }
}
