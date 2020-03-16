package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/14
 * time: 14:29
 * email: 694125155@qq.com
 */
public class BkTgqShowData implements Serializable {
    public int tgqFrom;//	int
    public String returnRule;//	String
    public String changeRule;//	String
    public boolean allowChange;//	boolean
    public boolean canCharge;//	boolean
    public boolean canRefund;//	boolean
    public boolean airlineTgq;//	boolean
    public String signText;//	String
    public List<BktgqPointChargesInfo> tgqPointCharges;// TgqPointCharge实例list
}
