package com.feitianzhu.huangliwo.pushshop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/1/14
 * time: 10:58
 * email: 694125155@qq.com
 */
public class SetMealInfo implements Serializable {

    /**
     * createTime : 2020-01-13T09:57:18.022Z
     * desc : string
     * discount : 0
     * imgs : string
     * isShelf : 0
     * merchantId : 0
     * price : 0
     * singleList : [{"id":0,"name":"string","num":0,"singlePrice":0,"smId":0},{"id":0,"name":"string","num":0,"singlePrice":0,"smId":0}]
     * smId : 0
     * smName : string
     * updateTime : 2020-01-13T09:57:18.022Z
     * useRules : string
     */

    private String createTime;
    private String remark;
    private double discount;
    private String imgs;
    private int isShelf;
    private int merchantId;
    private String userId;
    private double price;
    private int smId;
    private String smName;
    private String updateTime;
    private String useRules;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private List<SingleGoodsModel> singleList;

    public List<SingleGoodsModel> getSingleList() {
        return singleList;
    }

    public void setSingleList(List<SingleGoodsModel> singleList) {
        this.singleList = singleList;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public int getIsShelf() {
        return isShelf;
    }

    public void setIsShelf(int isShelf) {
        this.isShelf = isShelf;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSmId() {
        return smId;
    }

    public void setSmId(int smId) {
        this.smId = smId;
    }

    public String getSmName() {
        return smName;
    }

    public void setSmName(String smName) {
        this.smName = smName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUseRules() {
        return useRules;
    }

    public void setUseRules(String useRules) {
        this.useRules = useRules;
    }

}
