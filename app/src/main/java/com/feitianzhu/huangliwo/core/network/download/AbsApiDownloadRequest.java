package com.feitianzhu.huangliwo.core.network.download;


import com.lzy.okserver.download.DownloadTask;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbsApiDownloadRequest {

    public abstract String getAPIBaseURL();

    public abstract String getAPIName();

    public abstract ParamsBuilder appendParams(ParamsBuilder builder);



    public abstract DownloadTask call(final ApiCallBackDownload listener);

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
