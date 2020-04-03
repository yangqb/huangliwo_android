package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/3
 * time: 18:13
 * email: 694125155@qq.com
 */
public class BaseOrderInfo implements Serializable {
    public int status;
    public String statusDesc;
    public boolean showNotWork;
    public int distributeType;
}
