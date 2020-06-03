package com.feitianzhu.huangliwo.travel.bean;

public class OilListBean {


    /**
     * gasId : BJ000002225
     * gasName : 北京市公交马场加油站
     * gasLogoSmall : https://static.czb365.com/1590575010398.jpg?x-oss-process=image/resize,m_lfit,h_200,w_200/format,png
     * gasLogoBig : https://static.czb365.com/1590575010100.jpg?x-oss-process=image/resize,m_lfit,h_420,w_630/format,png
     * gasAddress : 北京市丰台区富丰园340总站内
     * distanceStr : 1km
     * distance : 1069
     * priceYfq : 4.80
     * priceOfficial : 5.50
     * priceGun : 5.20
     * gasAddressLongitude : 116.284476
     * gasAddressLatitude : 39.83672
     */

    private String gasId;
    private String gasName;
    private String gasLogoSmall;
    private String gasLogoBig;
    private String gasAddress;
    private String distanceStr;
    private int distance;
    private String priceYfq;
    private String priceOfficial;
    private String priceGun;
    private double gasAddressLongitude;
    private double gasAddressLatitude;

    public String getGasId() {
        return gasId;
    }

    public void setGasId(String gasId) {
        this.gasId = gasId;
    }

    public String getGasName() {
        return gasName;
    }

    public void setGasName(String gasName) {
        this.gasName = gasName;
    }

    public String getGasLogoSmall() {
        return gasLogoSmall;
    }

    public void setGasLogoSmall(String gasLogoSmall) {
        this.gasLogoSmall = gasLogoSmall;
    }

    public String getGasLogoBig() {
        return gasLogoBig;
    }

    public void setGasLogoBig(String gasLogoBig) {
        this.gasLogoBig = gasLogoBig;
    }

    public String getGasAddress() {
        return gasAddress;
    }

    public void setGasAddress(String gasAddress) {
        this.gasAddress = gasAddress;
    }

    public String getDistanceStr() {
        return distanceStr;
    }

    public void setDistanceStr(String distanceStr) {
        this.distanceStr = distanceStr;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getPriceYfq() {
        return priceYfq;
    }

    public void setPriceYfq(String priceYfq) {
        this.priceYfq = priceYfq;
    }

    public String getPriceOfficial() {
        return priceOfficial;
    }

    public void setPriceOfficial(String priceOfficial) {
        this.priceOfficial = priceOfficial;
    }

    public String getPriceGun() {
        return priceGun;
    }

    public void setPriceGun(String priceGun) {
        this.priceGun = priceGun;
    }

    public double getGasAddressLongitude() {
        return gasAddressLongitude;
    }

    public void setGasAddressLongitude(double gasAddressLongitude) {
        this.gasAddressLongitude = gasAddressLongitude;
    }

    public double getGasAddressLatitude() {
        return gasAddressLatitude;
    }

    public void setGasAddressLatitude(double gasAddressLatitude) {
        this.gasAddressLatitude = gasAddressLatitude;
    }
}
