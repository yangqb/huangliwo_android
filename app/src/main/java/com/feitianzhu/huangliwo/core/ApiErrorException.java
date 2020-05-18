package com.feitianzhu.huangliwo.core;

import com.feitianzhu.huangliwo.core.network.BaseApiResponse;

/**
 * Created by bch on 2020/5/11
 */
public class ApiErrorException extends RuntimeException {
    private int errorCode;
    private String errorMsg;

    public ApiErrorException() {
        super();
    }

    public ApiErrorException(BaseApiResponse rep) {
        errorCode = rep.getCode();
        errorMsg = rep.getMessage();
    }

    public ApiErrorException(int errorCode, String errorMsg, int errorType) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}