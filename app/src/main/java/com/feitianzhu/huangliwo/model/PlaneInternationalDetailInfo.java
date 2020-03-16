package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/11
 * time: 16:50
 * email: 694125155@qq.com
 */
public class PlaneInternationalDetailInfo implements Serializable {
    public GoBackTripInfo goTrip;
    public GoBackTripInfo backTrip;
    public List<InternationalPriceInfo> priceInfo;
}
