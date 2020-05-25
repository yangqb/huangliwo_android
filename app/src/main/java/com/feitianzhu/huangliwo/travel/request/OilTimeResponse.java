package com.feitianzhu.huangliwo.travel.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.feitianzhu.huangliwo.core.network.BaseApiResponse;

/**
 * Created by bch on 2020/5/20
 */
public class OilTimeResponse extends BaseApiResponse {
    @JSONField(name = "result")
    public Object data;

    @JSONField(name = "code")
    public int code = -1;
    //
    @JSONField(name = "message")
    public String message;
//
//    @JSONField(name = "ret")
//    public int ret;

    public Object getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRequestSuccess() {
        return (code == 200);
    }
}
