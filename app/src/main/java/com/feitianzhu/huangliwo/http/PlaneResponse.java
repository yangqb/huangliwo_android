package com.feitianzhu.huangliwo.http;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.http
 * user: yangqinbo
 * date: 2020/3/4
 * time: 20:29
 * email: 694125155@qq.com
 */
public class PlaneResponse<T> implements Serializable {
    public int code;
    public String message;
    public T result;
}
