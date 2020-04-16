package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/16
 * time: 10:15
 * email: 694125155@qq.com
 */
public class WXUserInfo implements Serializable {
    public String errcode;//": 40029,
    public String errmsg;//": "invalid code"
    public String openid;//": "OPENID",
    public String nickname;//": "NICKNAME",
    public int sex;//": 1,
    public String province;//": "PROVINCE",
    public String city;//": "CITY",
    public String country;//": "COUNTRY",
    public String headimgurl;//": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
    public List<String> privilege;//": ["PRIVILEGE1", "PRIVILEGE2"],
    public String unionid;//": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
}
