package com.feitianzhu.huangliwo.pushshop.bean;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/1/11
 * time: 15:00
 * email: 694125155@qq.com
 */
public class MerchantsModel implements Serializable {

    /**
     * userId : 849501
     * inviteCode : 275789
     * merchantId : 25
     * merchantName : 爱可以
     * registerNo : 888888
     * phone : 13100680322
     * clsId : 1
     * provinceId : 440000
     * provinceName : 广东省
     * cityId : 440300
     * cityName : 深圳市
     * areaId : 440306
     * areaName : 宝安区
     * dtlAddr : 固戍西乡梧桐岛
     * longitude : 0.0
     * latitude : 0.0
     * introduce : 伯母用
     * sequence : null
     * isClose : 0
     * logo : http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg,http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg,http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg,http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg,http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg
     * createDate : 2020-01-11 10:56:20
     * status : 1
     * examineBy : null
     * examineDate : null
     * refuseReason : null
     * updateBy : null
     * updateDate : null
     * clsName : 美食
     * email : 694125155@qq.com
     * discount : 0.8
     * shopFrontImg : http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg
     * shopInsideImg : http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg
     * cardFrontImg : http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg
     * cardBackImg : http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg
     * businessLicenseImg : http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg
     * permitImg : http://www.huangliwo.top:8088/images/1/images/goods/2019/04/05.jpg
     * distince : null
     * distinceStr : null
     * smsCode : null
     * income : null
     * balance : null
     */

    private int userId;
    private int inviteCode;
    private int merchantId;
    private String merchantName;
    private String registerNo;
    private String phone;
    private int clsId;
    private String provinceId;
    private String provinceName;
    private String cityId;
    private String cityName;
    private String areaId;
    private String areaName;
    private String dtlAddr;
    private String longitude;
    private String latitude;
    private String introduce;
    private String sequence;
    private int isClose;
    private String logo;
    private String createDate;
    private int status;
    private String examineBy;
    private String examineDate;
    private String refuseReason;
    private String updateBy;
    private String updateDate;
    private String clsName;
    private String email;
    private double discount;
    private String shopFrontImg;
    private String shopInsideImg;
    private String cardFrontImg;
    private String cardBackImg;
    private String businessLicenseImg;
    private String permitImg;
    private String distince;
    private String distinceStr;
    private String smsCode;
    private double income;
    private double balance;
    private String businessTime;
    private AuditModel examineModel;

    public AuditModel getExamineModel() {
        return examineModel;
    }

    public void setExamineModel(AuditModel examineModel) {
        this.examineModel = examineModel;
    }

    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(int inviteCode) {
        this.inviteCode = inviteCode;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getClsId() {
        return clsId;
    }

    public void setClsId(int clsId) {
        this.clsId = clsId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getDtlAddr() {
        return dtlAddr;
    }

    public void setDtlAddr(String dtlAddr) {
        this.dtlAddr = dtlAddr;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getIsClose() {
        return isClose;
    }

    public void setIsClose(int isClose) {
        this.isClose = isClose;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExamineBy() {
        return examineBy;
    }

    public void setExamineBy(String examineBy) {
        this.examineBy = examineBy;
    }

    public String getExamineDate() {
        return examineDate;
    }

    public void setExamineDate(String examineDate) {
        this.examineDate = examineDate;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getShopFrontImg() {
        return shopFrontImg;
    }

    public void setShopFrontImg(String shopFrontImg) {
        this.shopFrontImg = shopFrontImg;
    }

    public String getShopInsideImg() {
        return shopInsideImg;
    }

    public void setShopInsideImg(String shopInsideImg) {
        this.shopInsideImg = shopInsideImg;
    }

    public String getCardFrontImg() {
        return cardFrontImg;
    }

    public void setCardFrontImg(String cardFrontImg) {
        this.cardFrontImg = cardFrontImg;
    }

    public String getCardBackImg() {
        return cardBackImg;
    }

    public void setCardBackImg(String cardBackImg) {
        this.cardBackImg = cardBackImg;
    }

    public String getBusinessLicenseImg() {
        return businessLicenseImg;
    }

    public void setBusinessLicenseImg(String businessLicenseImg) {
        this.businessLicenseImg = businessLicenseImg;
    }

    public String getPermitImg() {
        return permitImg;
    }

    public void setPermitImg(String permitImg) {
        this.permitImg = permitImg;
    }

    public String getDistince() {
        return distince;
    }

    public void setDistince(String distince) {
        this.distince = distince;
    }

    public String getDistinceStr() {
        return distinceStr;
    }

    public void setDistinceStr(String distinceStr) {
        this.distinceStr = distinceStr;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static class AuditModel implements Serializable {
        private String backCardImg;// (string, optional): 身份证反面图片 ,
        private String blReason;// (string, optional): 营业执照审核拒绝原因 ,
        private int blStatus;// (integer, optional): 营业执照审核状态（0：审核中，1：审核通过，-1：拒绝） ,
        private String businessLicenseImg;// (string, optional): 营业执照图片 ,
        private String businessLicenseNum;//(string, optional): 营业执照号 ,
        private String cardReason;//(string, optional): 身份证审核拒绝原因 ,
        private int cardStatus;//(integer, optional): 身份证审核状态（0：审核中，1：审核通过，-1：拒绝） ,
        private String createDate;//(string, optional),
        private String examineBy;//(string, optional),
        private String examineDate;//(string, optional),
        private String frontCardImg;//(string, optional): 身份证正面图片 ,
        private int merchantId;//(integer, optional): 商户id ,
        private String merchantName;// (string, optional): 商户名称 ,
        private String updateDate;//(string, optional)
        private int dcStatus;  //折扣比例审核

        public int getDcStatus() {
            return dcStatus;
        }

        public void setDcStatus(int dcStatus) {
            this.dcStatus = dcStatus;
        }

        public String getBackCardImg() {
            return backCardImg;
        }

        public void setBackCardImg(String backCardImg) {
            this.backCardImg = backCardImg;
        }

        public String getBlReason() {
            return blReason;
        }

        public void setBlReason(String blReason) {
            this.blReason = blReason;
        }

        public int getBlStatus() {
            return blStatus;
        }

        public void setBlStatus(int blStatus) {
            this.blStatus = blStatus;
        }

        public String getBusinessLicenseImg() {
            return businessLicenseImg;
        }

        public void setBusinessLicenseImg(String businessLicenseImg) {
            this.businessLicenseImg = businessLicenseImg;
        }

        public String getBusinessLicenseNum() {
            return businessLicenseNum;
        }

        public void setBusinessLicenseNum(String businessLicenseNum) {
            this.businessLicenseNum = businessLicenseNum;
        }

        public String getCardReason() {
            return cardReason;
        }

        public void setCardReason(String cardReason) {
            this.cardReason = cardReason;
        }

        public int getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(int cardStatus) {
            this.cardStatus = cardStatus;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getExamineBy() {
            return examineBy;
        }

        public void setExamineBy(String examineBy) {
            this.examineBy = examineBy;
        }

        public String getExamineDate() {
            return examineDate;
        }

        public void setExamineDate(String examineDate) {
            this.examineDate = examineDate;
        }

        public String getFrontCardImg() {
            return frontCardImg;
        }

        public void setFrontCardImg(String frontCardImg) {
            this.frontCardImg = frontCardImg;
        }

        public int getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(int merchantId) {
            this.merchantId = merchantId;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }
    }


}
