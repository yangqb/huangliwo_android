package com.feitianzhu.huangliwo.model;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/17
 * time: 15:41
 * email: 694125155@qq.com
 */
public class ShoppingCartMode {
    private boolean isSelect;
    private double price;
    private int count;
    private String attributeVal;

    public String getAttributeVal() {
        return attributeVal;
    }

    public void setAttributeVal(String attributeVal) {
        this.attributeVal = attributeVal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

}
