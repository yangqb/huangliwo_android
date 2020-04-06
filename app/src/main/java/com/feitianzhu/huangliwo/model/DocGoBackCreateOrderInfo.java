package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/6
 * time: 14:35
 * email: 694125155@qq.com
 */
public class DocGoBackCreateOrderInfo implements Serializable {
    public int Code;
    public String noPayAmount;
    public List<DocGoBackCreateOrderModel> orders;
}
