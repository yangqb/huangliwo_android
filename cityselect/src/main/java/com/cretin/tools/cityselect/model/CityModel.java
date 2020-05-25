package com.cretin.tools.cityselect.model;

import java.io.Serializable;

/**
 * @date: on 2019-10-30
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 用户需要传入的数据
 */
public class CityModel implements Serializable {
    private String cityName;
    private Object extra;
    private String countryName;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public CityModel() {
    }

    public CityModel(String cityName, String countryName, Object extra) {
        this.cityName = cityName;
        this.extra = extra;
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

}
