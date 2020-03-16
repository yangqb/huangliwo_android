package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/5
 * time: 20:14
 * email: 694125155@qq.com
 */
public class TGGShowInfo implements Serializable {
    public int tgqFrom;
    public String returnRule;
    public String changeRule;
    public boolean canRefund;
    public boolean airlineTgq;
    public List<TgqPointChargesInfo> tgqPointCharges;
}
