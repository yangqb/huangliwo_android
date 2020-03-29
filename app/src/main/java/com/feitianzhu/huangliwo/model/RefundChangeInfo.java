package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/12
 * time: 18:48
 * email: 694125155@qq.com
 */
public class RefundChangeInfo implements Serializable {
    public List<TgqPointCharges> tgqPointCharges;//":Array[4],
    //public List<CTgqPointCharges> cTgqPointCharges;
    public int tgqForm;//":0,
    public String returnRule;//":"20-168-30-48-70-4-90",
    public boolean allowChange;//":false,
    public boolean canCharge;//":true,
    public boolean canrefund;//":false,
    public String tgqText;//":"退票手续费收取规则：航班起飞前168小时之前，退票手续费为80元；航班起飞前48小时之前，退票手续费为120元；航班起飞前4小时之前，退票手续费为280元；航班起飞前4小时之后，退票手续费为360元<br />同航司改期手续费收取规则：航班起飞前168小时之前，改期手续费为40元；航班起飞前48小时之前，改期手续费为80元；航班起飞前4小时之前，改期手续费为200元；航班起飞前4小时之后，改期手续费为280元<br />签转条件：不可签转",
    public boolean airlineTgq;//":"true",
    public String signText;//":"不可签转",
    public boolean hasTime;//":true,
    public double basePrice;//":400
    public String tgqCabin;
    public int viewType;
    public String tgqPercentText;
    public String tgqProduct;
    public String returnText;
    public String changeText;
    public String changeRule;
}
