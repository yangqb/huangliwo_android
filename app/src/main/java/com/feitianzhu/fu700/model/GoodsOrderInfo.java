package com.feitianzhu.fu700.model;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/12
 * time: 18:47
 * email: 694125155@qq.com
 */
public class GoodsOrderInfo implements Serializable {
    /*
     * 1 未支付，2 待发货，3 待收货(已发货)，4 已完成（已收货），5 退款中，6 已退款，7 订单取消（未支付的）
     * */
    public static final int TYPE_NO_PAY = 1;
    public static final int TYPE_WAIT_DELIVERY = 2;
    public static final int TYPE_WAIT_RECEIVING = 3;
    public static final int TYPE_COMPLETED = 4;
    public static final int TYPE_REFUND = 5;
    public static final int TYPE_REFUNDED = 6;
    public static final int TYPE_CANCEL = 7;

    private String accessToken;
    private String userId;
    private List<GoodsOrderListBean> goodsOrderList;

    public List<GoodsOrderListBean> getGoodsOrderList() {
        return goodsOrderList;
    }

    public void setGoodsOrderList(List<GoodsOrderListBean> goodsOrderList) {
        this.goodsOrderList = goodsOrderList;
    }

    public static class GoodsOrderListBean implements Serializable {
        /**
         * orderNo : BSB20191213190034166524
         * userId : 14
         * amount : 0.01
         * postage : 0.1
         * rebatePv : 0.01
         * type : 1
         * channel : alipay
         * thirdOrderNo : null
         * payProof : null
         * createDate : 2019-12-13 19:01:53
         * payDate : 2019-12-13 19:01:53
         * deliveryDate : null
         * receiptDate : null
         * addressId : null
         * remark : null
         * status : 0 //1 未支付，2 待发货，3 待收货，4 已完成（已收货），5 退款中，6 已退款，7 订单取消（未支付的）
         * isPoints : null
         * isExtend : null
         * logisticsNo : null
         * logisticsCode : null
         * orderDetailList : null
         * shopAddress : [{"addressId":null,"userId":14,"provinceId":null,"cityId":null,"areaId":null,"areaName":null,"detailAddress":null,"userName":null,"phone":null,"isDefalt":null}]
         * receiptName : 我摸
         * receiptPhone : 1310060321
         * regionName : null
         * detailAddr : 就去莫莫莫
         * leaveMsg : null
         * statusList : null
         * buyerName : null
         * buyerPhone : null
         * buyerEmail : null
         * parentId : 4
         * parentName : HLW0321
         * parentPhone : 13100680321
         * parentEmail : null
         * payAccount : null
         * provinceId : null
         * provinceName : null
         * norms : null
         * goodsName : 66666
         * goodsImg : null
         * valueId : 2
         * goodsQTY : 1
         * summary : 66
         * title : null
         * shopName : null
         */
        private String orderNo;
        private int userId;
        private double amount;
        private double postage;
        private double rebatePv;
        private int type;
        private String channel;
        private String thirdOrderNo;
        private String payProof;
        private String createDate;
        private String payDate;
        private String deliveryDate;
        private String receiptDate;
        private String addressId;
        private String remark;
        private int status;
        private String isPoints;
        private String isExtend;
        private String logisticsNo;
        private String logisticsCode;
        private String orderDetailList;
        private String receiptName;
        private String receiptPhone;
        private String regionName;
        private String detailAddr;
        private String leaveMsg;
        private String statusList;
        private String buyerName;
        private String buyerPhone;
        private String buyerEmail;
        private int parentId;
        private String parentName;
        private String parentPhone;
        private String parentEmail;
        private String payAccount;
        private String provinceId;
        private String provinceName;
        private String norms;
        private String goodsName;
        private String goodsImg;
        private int valueId;
        private int goodsQTY;
        private String summary;
        private String title;
        private String shopName;
        private double price;
        private ShopAddressBean shopAddress;

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getPostage() {
            return postage;
        }

        public void setPostage(double postage) {
            this.postage = postage;
        }

        public double getRebatePv() {
            return rebatePv;
        }

        public void setRebatePv(double rebatePv) {
            this.rebatePv = rebatePv;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getThirdOrderNo() {
            return thirdOrderNo;
        }

        public void setThirdOrderNo(String thirdOrderNo) {
            this.thirdOrderNo = thirdOrderNo;
        }

        public String getPayProof() {
            return payProof;
        }

        public void setPayProof(String payProof) {
            this.payProof = payProof;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getPayDate() {
            return payDate;
        }

        public void setPayDate(String payDate) {
            this.payDate = payDate;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public String getReceiptDate() {
            return receiptDate;
        }

        public void setReceiptDate(String receiptDate) {
            this.receiptDate = receiptDate;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getIsPoints() {
            return isPoints;
        }

        public void setIsPoints(String isPoints) {
            this.isPoints = isPoints;
        }

        public String getIsExtend() {
            return isExtend;
        }

        public void setIsExtend(String isExtend) {
            this.isExtend = isExtend;
        }

        public String getLogisticsNo() {
            return logisticsNo;
        }

        public void setLogisticsNo(String logisticsNo) {
            this.logisticsNo = logisticsNo;
        }

        public String getLogisticsCode() {
            return logisticsCode;
        }

        public void setLogisticsCode(String logisticsCode) {
            this.logisticsCode = logisticsCode;
        }

        public String getOrderDetailList() {
            return orderDetailList;
        }

        public void setOrderDetailList(String orderDetailList) {
            this.orderDetailList = orderDetailList;
        }

        public String getReceiptName() {
            return receiptName;
        }

        public void setReceiptName(String receiptName) {
            this.receiptName = receiptName;
        }

        public String getReceiptPhone() {
            return receiptPhone;
        }

        public void setReceiptPhone(String receiptPhone) {
            this.receiptPhone = receiptPhone;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }

        public String getDetailAddr() {
            return detailAddr;
        }

        public void setDetailAddr(String detailAddr) {
            this.detailAddr = detailAddr;
        }

        public String getLeaveMsg() {
            return leaveMsg;
        }

        public void setLeaveMsg(String leaveMsg) {
            this.leaveMsg = leaveMsg;
        }

        public String getStatusList() {
            return statusList;
        }

        public void setStatusList(String statusList) {
            this.statusList = statusList;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public String getBuyerPhone() {
            return buyerPhone;
        }

        public void setBuyerPhone(String buyerPhone) {
            this.buyerPhone = buyerPhone;
        }

        public String getBuyerEmail() {
            return buyerEmail;
        }

        public void setBuyerEmail(String buyerEmail) {
            this.buyerEmail = buyerEmail;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getParentName() {
            return parentName;
        }

        public void setParentName(String parentName) {
            this.parentName = parentName;
        }

        public String getParentPhone() {
            return parentPhone;
        }

        public void setParentPhone(String parentPhone) {
            this.parentPhone = parentPhone;
        }

        public String getParentEmail() {
            return parentEmail;
        }

        public void setParentEmail(String parentEmail) {
            this.parentEmail = parentEmail;
        }

        public String getPayAccount() {
            return payAccount;
        }

        public void setPayAccount(String payAccount) {
            this.payAccount = payAccount;
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

        public String getNorms() {
            return norms;
        }

        public void setNorms(String norms) {
            this.norms = norms;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public int getValueId() {
            return valueId;
        }

        public void setValueId(int valueId) {
            this.valueId = valueId;
        }

        public int getGoodsQTY() {
            return goodsQTY;
        }

        public void setGoodsQTY(int goodsQTY) {
            this.goodsQTY = goodsQTY;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public ShopAddressBean getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(ShopAddressBean shopAddress) {
            this.shopAddress = shopAddress;
        }

        public static class ShopAddressBean implements Serializable {
            /**
             * addressId : null
             * userId : 14
             * provinceId : null
             * cityId : null
             * areaId : null
             * areaName : null
             * detailAddress : null
             * userName : null
             * phone : null
             * isDefalt : null
             */

            private String addressId;
            private int userId;
            private String provinceId;
            private String cityId;
            private String areaId;
            private String areaName;
            private String detailAddress;
            private String userName;
            private String phone;
            private int isDefalt;

            public String getAddressId() {
                return addressId;
            }

            public void setAddressId(String addressId) {
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
}
