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
        private Object thirdOrderNo;
        private Object payProof;
        private String createDate;
        private String payDate;
        private Object deliveryDate;
        private Object receiptDate;
        private Object addressId;
        private Object remark;
        private int status;
        private Object isPoints;
        private Object isExtend;
        private Object logisticsNo;
        private Object logisticsCode;
        private Object orderDetailList;
        private String receiptName;
        private String receiptPhone;
        private Object regionName;
        private String detailAddr;
        private Object leaveMsg;
        private Object statusList;
        private Object buyerName;
        private Object buyerPhone;
        private Object buyerEmail;
        private int parentId;
        private String parentName;
        private String parentPhone;
        private Object parentEmail;
        private Object payAccount;
        private Object provinceId;
        private Object provinceName;
        private Object norms;
        private String goodsName;
        private Object goodsImg;
        private int valueId;
        private int goodsQTY;
        private String summary;
        private Object title;
        private Object shopName;
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

        public Object getThirdOrderNo() {
            return thirdOrderNo;
        }

        public void setThirdOrderNo(Object thirdOrderNo) {
            this.thirdOrderNo = thirdOrderNo;
        }

        public Object getPayProof() {
            return payProof;
        }

        public void setPayProof(Object payProof) {
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

        public Object getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(Object deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public Object getReceiptDate() {
            return receiptDate;
        }

        public void setReceiptDate(Object receiptDate) {
            this.receiptDate = receiptDate;
        }

        public Object getAddressId() {
            return addressId;
        }

        public void setAddressId(Object addressId) {
            this.addressId = addressId;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getIsPoints() {
            return isPoints;
        }

        public void setIsPoints(Object isPoints) {
            this.isPoints = isPoints;
        }

        public Object getIsExtend() {
            return isExtend;
        }

        public void setIsExtend(Object isExtend) {
            this.isExtend = isExtend;
        }

        public Object getLogisticsNo() {
            return logisticsNo;
        }

        public void setLogisticsNo(Object logisticsNo) {
            this.logisticsNo = logisticsNo;
        }

        public Object getLogisticsCode() {
            return logisticsCode;
        }

        public void setLogisticsCode(Object logisticsCode) {
            this.logisticsCode = logisticsCode;
        }

        public Object getOrderDetailList() {
            return orderDetailList;
        }

        public void setOrderDetailList(Object orderDetailList) {
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

        public Object getRegionName() {
            return regionName;
        }

        public void setRegionName(Object regionName) {
            this.regionName = regionName;
        }

        public String getDetailAddr() {
            return detailAddr;
        }

        public void setDetailAddr(String detailAddr) {
            this.detailAddr = detailAddr;
        }

        public Object getLeaveMsg() {
            return leaveMsg;
        }

        public void setLeaveMsg(Object leaveMsg) {
            this.leaveMsg = leaveMsg;
        }

        public Object getStatusList() {
            return statusList;
        }

        public void setStatusList(Object statusList) {
            this.statusList = statusList;
        }

        public Object getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(Object buyerName) {
            this.buyerName = buyerName;
        }

        public Object getBuyerPhone() {
            return buyerPhone;
        }

        public void setBuyerPhone(Object buyerPhone) {
            this.buyerPhone = buyerPhone;
        }

        public Object getBuyerEmail() {
            return buyerEmail;
        }

        public void setBuyerEmail(Object buyerEmail) {
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

        public Object getParentEmail() {
            return parentEmail;
        }

        public void setParentEmail(Object parentEmail) {
            this.parentEmail = parentEmail;
        }

        public Object getPayAccount() {
            return payAccount;
        }

        public void setPayAccount(Object payAccount) {
            this.payAccount = payAccount;
        }

        public Object getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(Object provinceId) {
            this.provinceId = provinceId;
        }

        public Object getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(Object provinceName) {
            this.provinceName = provinceName;
        }

        public Object getNorms() {
            return norms;
        }

        public void setNorms(Object norms) {
            this.norms = norms;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public Object getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(Object goodsImg) {
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

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public Object getShopName() {
            return shopName;
        }

        public void setShopName(Object shopName) {
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
