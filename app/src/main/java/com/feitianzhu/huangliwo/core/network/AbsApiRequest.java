package com.feitianzhu.huangliwo.core.network;


import com.alibaba.fastjson.TypeReference;

import java.util.LinkedHashMap;
import java.util.Map;

import rx.Subscription;

public abstract class AbsApiRequest {

    public abstract String getAPIBaseURL();

    public abstract String getAPIName();

    public abstract ParamsBuilder appendParams(ParamsBuilder builder);

    public abstract TypeReference getResponseType();

    public abstract TypeReference getDatatype();

    public abstract Subscription call(final ApiCallBack listener);

    public abstract void cancelRequest();

    public static final class ParamsBuilder {

        private Map<String, Object> params = new LinkedHashMap<String, Object>();

        public ParamsBuilder append(String key, Object value) {
            params.put(key, value);
            return this;
        }

        public Map<String, Object> build() {
            return params;
        }
    }
}
