package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/14
 * time: 14:33
 * email: 694125155@qq.com
 */
public class BkBaggageRuleInfo implements Serializable {
    public String checkedBaggageRule;
    public String cabinBaggageRule;
    public String infantBaggageRule;
    public List<String> specialRule;
}
