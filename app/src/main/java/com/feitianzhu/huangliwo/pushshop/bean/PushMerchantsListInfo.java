package com.feitianzhu.huangliwo.pushshop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/1/11
 * time: 11:15
 * email: 694125155@qq.com
 */
public class PushMerchantsListInfo implements Serializable {
    private int unPassedCount;//":1,
    public List<MerchantsModel> examineList;//":[], 审核中
    private int examineCount;//":1,
    private int passedCount;//:1,
    public List<MerchantsModel> passedList;//":[], 通过
    public List<MerchantsModel> unPassedList;//":[]未通过

    public int getUnPassedCount() {
        return unPassedCount;
    }

    public void setUnPassedCount(int unPassedCount) {
        this.unPassedCount = unPassedCount;
    }


    public int getExamineCount() {
        return examineCount;
    }

    public void setExamineCount(int examineCount) {
        this.examineCount = examineCount;
    }

    public int getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(int passedCount) {
        this.passedCount = passedCount;
    }

}
