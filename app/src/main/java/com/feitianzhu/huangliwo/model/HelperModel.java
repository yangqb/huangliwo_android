package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/22
 * time: 14:47
 * email: 694125155@qq.com
 */
public class HelperModel implements Serializable {
    public String createTime;//": "string",
    public String helpId;//": 0,
    public String helpImg;//": "string",
    public String isRelease;//": 0,
    public String modifyTime;//": "2020-04-22T04:10:03.016Z",
    public String remarks;//": "string",
    public String serviceTime;//": "string",
    public String title;//": "string"
    public int type;// type  1 常见问题 2客服电话 3二维码图片
    public String telephone;
    public String qrcode;
}
