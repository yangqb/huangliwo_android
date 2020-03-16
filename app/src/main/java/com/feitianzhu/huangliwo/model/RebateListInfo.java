package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/3/10
 * time: 19:03
 * email: 694125155@qq.com
 */
public class RebateListInfo implements Serializable {
    public double balanceAmount;// (number, optional),

    public String consumeOrderNo;//(string, optional),

    public String createDate;//(string, optional),

    public String isComplete;//(string, optional),

    public String isIncome;//(string, optional),

    public int plusCount;//(integer, optional),

    public double rate;//(number, optional),

    public double rebateAmount;//(number, optional),

    public int rebateCount;//(integer, optional),

    public int rebateId;//(integer, optional),

    public String rebateWay;//(string, optional),

    public String sourceOrderNo;//(string, optional),

    public double totalAmount;//(number, optional),

    public String type;//(string, optional),

    public String updateDate;//(string, optional),

    public int userId;//(integer, optional)
}
