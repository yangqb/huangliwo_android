package com.feitianzhu.huangliwo.core.network;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by bch on 2020/5/11
 * 通用请求返回公共处理类
 */
public class BaseTraveResponse extends BaseApiResponse {
    @JSONField(name = "result")
    public Object data;

    @JSONField(name = "code")
    public int code = -1;
//
    @JSONField(name = "message")
    public String msg;
//
//    @JSONField(name = "ret")
//    public int ret;

    public Object getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage(){
        return msg;
    }

    public boolean isRequestSuccess() {
        return (code == 0);
    }
}
