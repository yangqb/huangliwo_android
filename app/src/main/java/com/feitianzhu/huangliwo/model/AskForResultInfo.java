package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/4
 * time: 18:55
 * email: 694125155@qq.com
 */
public class AskForResultInfo implements Serializable {
    /*
    true：已经预生单，可以直接支付；
     false：包邮情况为false；
    * */
    public boolean needPay;
}
