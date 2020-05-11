package com.feitianzhu.huangliwo.core.network;

import com.lzy.okgo.model.HttpParams;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by bch on 2020/5/11
 * 网络请求参数对象
 */
public class ParamsBuilder {
    private Map<String, Object> params = new LinkedHashMap<String, Object>();

    public ParamsBuilder append(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return params;
    }

    public static HttpParams getHttpParams(Map<String, Object> params) {
        HttpParams httpParams = new HttpParams();
//        Map<String, Object> mparams = appendSignature(params);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() instanceof File) {
                if (entry.getValue() != null) {
                    httpParams.put(entry.getKey(), (File) entry.getValue());
                } else {
                }
            } else {
                /*if (entry.getValue() instanceof ArrayList) {
                    ArrayList value = (ArrayList) entry.getValue();
                    if (value.size() > 0 && value.get(0) instanceof File) {
                        httpParams.put(entry.getKey(), value);//
                    }
                }*/
                httpParams.put(entry.getKey(), getStringValue(entry.getValue()));//
            }
        }
        return httpParams;
    }

    private static String getStringValue(Object value) {
        if (value != null) {
            return value.toString();
        }
        return "";
    }
}
