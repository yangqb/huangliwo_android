package com.feitianzhu.huangliwo.core.network;

import com.alibaba.fastjson.TypeReference;
import com.lzy.okgo.model.HttpHeaders;

import java.util.LinkedHashMap;
import java.util.Map;

import rx.Subscription;

/**
 * Created by bch on 2020/5/11
 */
public abstract class AbsApiRequest {

    /**
     * 获取域名
     *
     * @return
     */
    public abstract String getAPIBaseURL();

    /**
     * 获取域名后网址
     *
     * @return
     */
    public abstract String getAPIName();

    /**
     * 拼接域名和网址
     *
     * @return
     */
    public String getApiUrl() {
        return getAPIBaseURL() + getAPIName();
    }

    /**
     * 拼接公共参数
     */
    protected ParamsBuilder appendBaseParams(ParamsBuilder builder) {
        return builder;
    }

    /**
     * 添加参数
     *
     * @param builder
     * @return
     */
    public abstract ParamsBuilder appendParams(ParamsBuilder builder);

    /**
     * 添加头参数
     *
     * @return
     */
    public abstract HttpHeaders addHeads(HttpHeaders headers);

    /**
     * 是否使用post请求
     * //TODO 现在先只支持get和post请求 ,其他请求以后优化 2020-5-11
     *
     * @return
     */
    public abstract boolean usePost();

    /**
     * 服务器返回的json统一处理类
     *
     * @return
     */
    public abstract TypeReference getResponseType();

    /**
     * json统一处理完之后第二次处理为详细model
     *
     * @return
     */
    public abstract TypeReference getDatatype();

    /**
     * 错误处理
     *
     * @param listener
     * @param errorCode
     * @param errorMsg
     */
    public abstract void handleError(ApiCallBack listener, int errorCode, String errorMsg);

    /**
     * 发起请求
     *
     * @param listener
     * @return
     */
    public abstract Subscription call(final ApiCallBack listener);

    /**
     * 取消请求
     */
    public abstract void cancelRequest();

}