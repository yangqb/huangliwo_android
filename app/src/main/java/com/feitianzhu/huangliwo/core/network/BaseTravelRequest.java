package com.feitianzhu.huangliwo.core.network;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.utils.Urls;

/**
 * Created by bch on 2020/6/4
 * 为什么需要这个基类,
 * 切换域名
 */
public abstract class BaseTravelRequest extends BaseRequest {
    /**
     * 设置域名 端口
     *
     * @return
     */
    @Override
    public String getAPIBaseURL() {
//        return "http://192.168.0.142:8087/";
        return Urls.TICKET_BASE_URL;
    }

    /**
     * 设置base返回基类,进行统一处理
     *
     * @return
     */
    @Override
    public TypeReference getResponseType() {
        return new TypeReference<BaseTraveResponse>() {
        };
    }

    @Override
    public void handleError(int errorCode, String errorMsg) {
        super.handleError(errorCode, errorMsg);

    }
}
