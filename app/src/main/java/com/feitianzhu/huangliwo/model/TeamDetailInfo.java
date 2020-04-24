package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/23
 * time: 18:57
 * email: 694125155@qq.com
 */
public class TeamDetailInfo implements Serializable {
    public List<TeamModel> vipList;// (Array[用户表], optional): 线下人员 ,
    public List<TeamModel> svipList;// (Array[用户表], optional): 线下人员 ,
    public List<TeamModel> consumeList;// (Array[用户表], optional): 线下人员 ,
}
