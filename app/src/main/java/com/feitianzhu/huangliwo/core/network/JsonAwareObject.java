package com.feitianzhu.huangliwo.core.network;


import com.alibaba.fastjson.JSON;

//TODO
public class JsonAwareObject {

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public byte[] toBytes() {
        return JSON.toJSONBytes(this);
    }
}
