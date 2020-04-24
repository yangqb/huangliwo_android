package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/10
 * time: 18:53
 * email: 694125155@qq.com
 */
public class MyTeamInfo implements Serializable {
    public String addrName;// (string, optional): 代理区域地址名称 ,
    public List<TeamModel> listUser;// (Array[用户表], optional): 线下人员 ,
    public int memberNum;// (integer, optional): 会员总数 ,
    public int registerNum;// (integer, optional): 注册人总数 ,
    public String teamAgent;// (string, optional): 代理区域 ,
    public String teamImg;// (string, optional): 团队长头像 ,
    public int teamNum;// (integer, optional): 团队数量
    public int yesdayAddCount;//@ApiModelProperty("昨日新增人数")
    public double monthIncome;//@ApiModelProperty("本月团队业绩")
    public double yesdayIncome;//@ApiModelProperty("昨日团队业绩")
    public TeamDetailInfo map;
}
