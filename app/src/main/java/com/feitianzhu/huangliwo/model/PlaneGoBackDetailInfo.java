package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/19
 * time: 15:04
 * email: 694125155@qq.com
 */
public class PlaneGoBackDetailInfo implements Serializable {
    public List<GoBackVendors> packVendors;
    public GoBackFlight go;
    public GoBackFlight back;

}
