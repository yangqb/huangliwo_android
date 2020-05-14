package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/9
 * time: 19:28
 * email: 694125155@qq.com
 */
public class AddressInfo implements Serializable {

    private List<ShopAddressListBean> shopAddressList;

    public List<ShopAddressListBean> getShopAddressList() {
        return shopAddressList;
    }

    public void setShopAddressList(List<ShopAddressListBean> shopAddressList) {
        this.shopAddressList = shopAddressList;
    }

    public static class ShopAddressListBean implements Serializable {
        /**
         * addressId : 1
         * userId : 4
         * provinceId : 1
         * cityId : 1
         * areaId : 1
         * areaName : 1
         * detailAddress : 1
         * userName : 1
         * phone : 13430456743
         * isDefalt : 1
         */

        private int addressId;
        private int userId;
        private String provinceId;
        private String provinceName;
        private String cityName;
        private String cityId;
        private String areaId;
        private String areaName;
        private String detailAddress;
        private String userName;
        private String phone;
        private int isDefalt;

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

        public int getAddressId() {
            return addressId;
        }

        public void setAddressId(int addressId) {
            this.addressId = addressId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getIsDefalt() {
            return isDefalt;
        }

        public void setIsDefalt(int isDefalt) {
            this.isDefalt = isDefalt;
        }
    }
}
