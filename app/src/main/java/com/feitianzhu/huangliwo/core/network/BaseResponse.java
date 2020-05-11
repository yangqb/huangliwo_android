package com.feitianzhu.huangliwo.core.network;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by bch on 2020/5/11
 */
public class BaseResponse extends BaseApiResponse {
    @JSONField(name = "data")
    public Object data;

    @JSONField(name = "code")
    public int code;

    @JSONField(name = "msg")
    public String msg;

    @JSONField(name = "ret")
    public int ret;

    public Object getData(){
        return data;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return msg;
    }

    public boolean isRequestSuccess(){
        return (code == 200);
    }
}
