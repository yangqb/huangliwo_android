package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/6
 * time: 18:06
 * email: 694125155@qq.com
 */
public class WXLoginModel implements Serializable {
    public String errcode;//": 40029,
    public String errmsg;//": "invalid code"
    public String access_token;//": "ACCESS_TOKEN",
    public String expires_in;//": 7200,
    public String refresh_token;//": "REFRESH_TOKEN",
    public String openid;//": "OPENID",
    public String scope;//": "SCOPE",
    public String unionid;//": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
}
