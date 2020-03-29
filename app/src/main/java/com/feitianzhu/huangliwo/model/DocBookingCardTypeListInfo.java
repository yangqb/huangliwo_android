package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/25
 * time: 17:31
 * email: 694125155@qq.com
 */
public class DocBookingCardTypeListInfo implements Serializable {
    public String type;
    public String name;
    public String ageLimit;
    public List<DocBookingRulesInfo> rules;
}
