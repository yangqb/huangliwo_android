package com.feitianzhu.huangliwo.travel.model;

import java.util.List;

/**
 * Created by bch on 2020/5/20
 */
public class OilStationsDetailBean {

    /**
     * gasId : QP000001559
     * gasName : 壳牌天马加油站（R70）
     * gasType : null
     * provinceCode : null
     * cityCode : null
     * countyCode : null
     * provinceName : null
     * cityName : null
     * countyName : null
     * gasLogoSmall : null
     * gasLogoBig : null
     * gasAddress : null
     * gasAddressLongitude : null
     * gasAddressLatitude : null
     * isInvoice : null
     * distance : null
     * distanceStr : null
     * oilPriceList : [{"oilNo":0,"oilType":2,"oilName":"0#","priceYfq":"6.64","priceGun":"6.74","priceOfficial":"7.02","gunNos":[{"gunNo":302},{"gunNo":402},{"gunNo":702},{"gunNo":802}]},{"oilNo":92,"oilType":1,"oilName":"92#","priceYfq":"5.90","priceGun":"6.00","priceOfficial":"7.38","gunNos":[{"gunNo":1},{"gunNo":2},{"gunNo":301},{"gunNo":401},{"gunNo":5},{"gunNo":6},{"gunNo":701},{"gunNo":801},{"gunNo":901},{"gunNo":1001},{"gunNo":11},{"gunNo":12},{"gunNo":1301},{"gunNo":1401},{"gunNo":15},{"gunNo":16}]},{"oilNo":95,"oilType":1,"oilName":"95#","priceYfq":"7.70","priceGun":"7.80","priceOfficial":"7.80","gunNos":[{"gunNo":902},{"gunNo":1002},{"gunNo":1302},{"gunNo":1402}]}]
     */

    private String gasId;
    private String gasName;
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
         * gunNos : [{"gunNo":302},{"gunNo":402},{"gunNo":702},{"gunNo":802}]
         */
//        油号名称
        private int oilNo;
        //        油号类别: 1汽油，2柴油，3天然气
        private int oilType;
        //        油号名称
        private String oilName;
        //        团油价格
        private String priceYfq;
        //        枪价
        private String priceGun;
        //        国标价
        private String priceOfficial;
        //        枪号
        private List<GunNosBean> gunNos;

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

        public List<GunNosBean> getGunNos() {
            return gunNos;
        }

        public void setGunNos(List<GunNosBean> gunNos) {
            this.gunNos = gunNos;
        }

        public static class GunNosBean {
            /**
             * gunNo : 302
             */

            private int gunNo;

            public int getGunNo() {
                return gunNo;
            }

            public void setGunNo(int gunNo) {
                this.gunNo = gunNo;
            }
        }
    }
}
