package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/4/3
 * time: 18:03
 * email: 694125155@qq.com
 */
public class NationalPassengerInfo implements Serializable {
    public long id;
    public String name;
    public String cardType;
    public String cardNum;
    public String ticketNum;
    public String birthday;
    public int gender;
    public RefundSearchResultInfo refundSearchResult;// Object
    public RefundApplyResultInfo refundApplyResult;//Object
    public ChangeSearchResultInfo changeSearchResult;// Object
    public ChangeApplyResultInfo changeApplyResult;// Object
}
