package com.feitianzhu.huangliwo.travel.bean;

import java.util.List;

public class OilListBean {

    /**
     * gasId : QP000001559
     * gasName : 壳牌天马加油站（R70）
     * gasType : 3
     * provinceCode : 500000
     * cityCode : 500100
     * countyCode : 500106
     * provinceName : 重庆市
     * cityName : 市辖区
     * countyName : 沙坪坝区
     * gasLogoSmall : https://static.czb365.com/1523428537366.jpg?x-oss-process=image/resize,m_lfit,h_200,w_200/format,png
     * gasLogoBig : https://static.czb365.com/1523428533754.jpg?x-oss-process=image/resize,m_lfit,h_420,w_630/format,png
     * gasAddress : 陈家桥街道陈家桥村四社
     * gasAddressLongitude : 116.39739
     * gasAddressLatitude : 39.90886
     * isInvoice : 1
     * distance : 1475
     * distanceStr : 1km
     * oilPriceList : [{"oilNo":0,"oilType":2,"oilName":"0#","priceYfq":"6.64","priceGun":"6.74","priceOfficial":"7.02","gunNos":null},{"oilNo":92,"oilType":1,"oilName":"92#","priceYfq":"5.90","priceGun":"6.00","priceOfficial":"7.38","gunNos":null},{"oilNo":95,"oilType":1,"oilName":"95#","priceYfq":"7.70","priceGun":"7.80","priceOfficial":"7.80","gunNos":null}]
     */

    private String gasId;
    private String gasName;
    private int gasType;
    private int provinceCode;
    private int cityCode;
    private int countyCode;
    private String provinceName;
    private String cityName;
    private String countyName;
    private String gasLogoSmall;
    private String gasLogoBig;
    private String gasAddress;
    private double gasAddressLongitude;
    private double gasAddressLatitude;
    private int isInvoice;
    private int distance;
    private String distanceStr;
    private List<OilPriceListBean> oilPriceList;

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

    public int getGasType() {
        return gasType;
    }

    public void setGasType(int gasType) {
        this.gasType = gasType;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(int countyCode) {
        this.countyCode = countyCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
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

    public int getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(int isInvoice) {
        this.isInvoice = isInvoice;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getDistanceStr() {
        return distanceStr;
    }

    public void setDistanceStr(String distanceStr) {
        this.distanceStr = distanceStr;
    }

    public List<OilPriceListBean> getOilPriceList() {
        return oilPriceList;
    }

    public void setOilPriceList(List<OilPriceListBean> oilPriceList) {
        this.oilPriceList = oilPriceList;
    }

    public static class OilPriceListBean {
        /**
         * oilNo : 0
         * oilType : 2
         * oilName : 0#
         * priceYfq : 6.64
         * priceGun : 6.74
         * priceOfficial : 7.02
         * gunNos : null
         */

        private int oilNo;
        private int oilType;
        private String oilName;
        private String priceYfq;
        private String priceGun;
        private String priceOfficial;
        private Object gunNos;

        public int getOilNo() {
            return oilNo;
        }

        public void setOilNo(int oilNo) {
            this.oilNo = oilNo;
        }

        public int getOilType() {
            return oilType;
        }

        public void setOilType(int oilType) {
            this.oilType = oilType;
        }

        public String getOilName() {
            return oilName;
        }

        public void setOilName(String oilName) {
            this.oilName = oilName;
        }

        public String getPriceYfq() {
            return priceYfq;
        }

        public void setPriceYfq(String priceYfq) {
            this.priceYfq = priceYfq;
        }

        public String getPriceGun() {
            return priceGun;
        }

        public void setPriceGun(String priceGun) {
            this.priceGun = priceGun;
        }

        public String getPriceOfficial() {
            return priceOfficial;
        }

        public void setPriceOfficial(String priceOfficial) {
            this.priceOfficial = priceOfficial;
        }

        public Object getGunNos() {
            return gunNos;
        }

        public void setGunNos(Object gunNos) {
            this.gunNos = gunNos;
        }
    }
}
