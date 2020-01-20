package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.model
 * user: yangqinbo
 * date: 2020/1/20
 * time: 14:38
 * email: 694125155@qq.com
 */
public class BindingAliAccountModel implements Serializable {

    /**
     * bankCardId : null
     * bankCardNo : null
     * userId : null
     * realName : null
     * bankId : null
     * openSubbranch : null
     * createDate : null
     * bankName : null
     * icon : null
     * type : null
     */

    private int bankCardId;
    private String bankCardNo;
    private int userId;
    private String realName;
    private String bankId;
    private String openSubbranch;
    private String createDate;
    private String bankName;
    private String icon;
    private int type;

    public int getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(int bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getOpenSubbranch() {
        return openSubbranch;
    }

    public void setOpenSubbranch(String openSubbranch) {
        this.openSubbranch = openSubbranch;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
