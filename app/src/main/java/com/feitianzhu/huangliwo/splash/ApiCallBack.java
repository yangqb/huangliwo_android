package com.feitianzhu.huangliwo.splash;

public interface ApiCallBack<T> {
    public abstract void onAPIResponse(T response);
    public abstract void onAPIError(int errorCode, String errorMsg);
}
