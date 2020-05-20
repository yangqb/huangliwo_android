package com.feitianzhu.huangliwo.travel.model;

import com.feitianzhu.huangliwo.utils.StringUtils;

import java.util.List;

/**
 * Created by bch on 2020/5/20
 */
public class OilStationsDetailBean {


    /**
     * oilInfo : [{"oilNo":92,"oilType":1,"oilName":"92#","priceYfq":"5.90","priceGun":"6.00","priceOfficial":"7.38","gunNos":[{"gunNo":1},{"gunNo":2},{"gunNo":301},{"gunNo":401},{"gunNo":5},{"gunNo":6},{"gunNo":701},{"gunNo":801},{"gunNo":901},{"gunNo":1001},{"gunNo":11},{"gunNo":12},{"gunNo":1301},{"gunNo":1401},{"gunNo":15},{"gunNo":16}]},{"oilNo":95,"oilType":1,"oilName":"95#","priceYfq":"7.70","priceGun":"7.80","priceOfficial":"7.80","gunNos":[{"gunNo":902},{"gunNo":1002},{"gunNo":1302},{"gunNo":1402}]}]
     * oilType : 1
     */
//    油号类别: 1汽油，2柴油，3天然气
    private int oilType;

    private List<OilInfoBean> oilInfo;

    public int getOilType() {
        return oilType;
    }

    public String getOilTypeString() {
        String s;
        switch (oilType) {
            case 1:
                s = "汽油";
                break;
            case 2:
                s = "柴油";
                break;
            case 3:
                s = "天然气";
                break;
            default:
                s = "其他";
                break;
        }
        return s;
    }

    public void setOilType(int oilType) {
        this.oilType = oilType;
    }

    public List<OilInfoBean> getOilInfo() {
        return oilInfo;
    }

    public void setOilInfo(List<OilInfoBean> oilInfo) {
        this.oilInfo = oilInfo;
    }

    public static class OilInfoBean {
        /**
         * oilNo : 92
         * oilType : 1
         * oilName : 92#
         * priceYfq : 5.90
         * priceGun : 6.00
         * priceOfficial : 7.38
         * gunNos : [{"gunNo":1},{"gunNo":2},{"gunNo":301},{"gunNo":401},{"gunNo":5},{"gunNo":6},{"gunNo":701},{"gunNo":801},{"gunNo":901},{"gunNo":1001},{"gunNo":11},{"gunNo":12},{"gunNo":1301},{"gunNo":1401},{"gunNo":15},{"gunNo":16}]
         */
//        油号名称
        private int oilNo;

        //        枪号
        private List<GunNosBean> gunNos;
        //        油号类别: 1汽油，2柴油，3天然气
        private int oilType;
        //        油号名称
        private String oilName;
        //        团油价格
        private double priceYfq;
        //        枪价
        private String priceGun;
        //        国标价
        private double priceOfficial;

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

        public double getPriceYfq() {
            return priceYfq;
        }

        public void setPriceYfq(double priceYfq) {
            this.priceYfq = priceYfq;
        }

        public String getPriceGun() {
            return priceGun;
        }

        public void setPriceGun(String priceGun) {
            this.priceGun = priceGun;
        }

        public double getPriceOfficial() {
            return priceOfficial;
        }

        public void setPriceOfficial(double priceOfficial) {
            this.priceOfficial = priceOfficial;
        }

        public List<GunNosBean> getGunNos() {
            return gunNos;
        }

        public void setGunNos(List<GunNosBean> gunNos) {
            this.gunNos = gunNos;
        }

        public static class GunNosBean {
            /**
             * gunNo : 1
             */

            private String gunNo;

            public String getGunNo() {
                return gunNo;
            }

            public void setGunNo(String gunNo) {
                this.gunNo = gunNo;
            }
        }
    }
}
//    //        油号名称
//    private int oilNo;
//    //
//    private int oilType;
//    //        油号名称
//    private String oilName;
//    //        团油价格
//    private String priceYfq;
//    //        枪价
//    private String priceGun;
//    //        国标价
//    private String priceOfficial;
//    //        枪号
//    private List<OilPriceListBean.GunNosBean> gunNos;