package com.feitianzhu.huangliwo.core.network.download;

import com.gowithmi.mapworld.core.network.BaseApiResponse;

/**
 * Created by gundam on 16/11/7.
 */
public class ApiDownloadErrorException extends RuntimeException {
    private int errorCode;
    private String errorMsg;

    public ApiDownloadErrorException() {
        super();
    }

    public ApiDownloadErrorException(BaseApiResponse rep) {
        errorCode = rep.getCode();
        errorMsg = rep.getMessage();
    }

    public ApiDownloadErrorException(int errorCode, String errorMsg) {
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
