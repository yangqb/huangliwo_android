package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/10
 * time: 18:58
 * email: 694125155@qq.com
 */
public class TeamModel implements Serializable {
    public int accountType;//(integer, optional),

    public int addressId;//(integer, optional),

    public double balance;//(number, optional),

    public String centerId;//(string, optional),

    public String clientType;//(string, optional),

    public String deviceToken;//(string, optional),

    public String headImg;//(string, optional),

    public String isFrozen;//(string, optional),

    public int isRnAuth;//(integer, optional),

    public String loginDate;//(string, optional),

    public String loginIp;//(string, optional),

    public String nickName;//(string, optional),

    public String openid;//(string, optional),

    public int parentId;//(integer, optional),

    public String parentIds;//(string, optional),

    //public String parentUsers(Array[用户表], optional),

    public String password;//(string, optional),

    public String paypass;//(string, optional),

    public String phone;//(string, optional),

    public long registeDate;//(string, optional),

    public String rongyunToken;//(string, optional),

    public int subordinateCount;//(integer, optional):发展会员的数量 ,

    public double totalConsume;//(number, optional),

    public double totalPoints;//(number, optional),

    public String unionid;//(string, optional),

    public String userId;//(integer, optional),

    public MineInfoModel userInfo;//(UserInfo, optional),

    public int userType;//(integer, optional),

    public List<RebateListInfo> waitRebateList;//(Array[WaitRebate], optional)

}
