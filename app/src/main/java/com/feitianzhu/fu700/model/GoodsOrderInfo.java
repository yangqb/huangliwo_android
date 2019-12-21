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

    private List<GoodsOrderListBean> goodsOrderList;

    public List<GoodsOrderListBean> getGoodsOrderList() {
        return goodsOrderList;
    }

    public void setGoodsOrderList(List<GoodsOrderListBean> goodsOrderList) {
        this.goodsOrderList = goodsOrderList;
    }

    public static class GoodsOrderListBean implements Serializable {
     /*
     *      amount (number, optional): 总价格 ,
attributeVal (string, optional): 商品属性 ,
count (integer, optional): 数量 ,
createDate (string, optional): 下单时间 ,
expiresDate (integer, optional): 过期时间 ,
goodName (string, optional): 商品名称 ,
goodsImg (string, optional): 商品图片 ,
nowTimeStamp (integer, optional): 当前时间戳 ,
orderNo (string, optional): 订单编号 ,
price (number, optional): 单价 ,
shopAddress (收货地址, optional): 订单收货地址 ,
shopName (string, optional): 店铺名称 ,
status (integer, optional): 订单状态 1 未支付，2 待发货，3 待收货(已发货)，4 已完成（已收货），5 退款中，6 已退款，7 订单取消（未支付的） ,
valueId (integer, optional): 商品属性id
}收货地址 {
addressId (integer, optional): addressId ,
areaId (string, optional): 区id ,
areaName (string, optional): 区名称 ,
cityId (string, optional): 市id ,
cityName (string, optional): 市名称 ,
detailAddress (string, optional): 详细地址 ,
isDefalt (integer, optional): 是否是默认地址 0否 1是 ,
phone (string, optional): 电话号码 ,
provinceId (string, optional): 省id ,
provinceName (string, optional): 省名称 ,
userId (integer, optional): 用户id ,
userName (string, optional): 用户名
     * */

        private String attributeVal;
        private String orderNo;
        private int userId;
        private double amount;
        private double postage;
        private double rebatePv;
        private String channel;
        private String createDate;
        private long expiresDate;
        private long nowTimeStamp;
        private String addressId;
        private int status;
        private String goodName;
        private String goodsImg;
        private String valueId;
        private int count;
        private String shopName;
        private double price;
        private ShopAddressBean address;
        private int goodId;
        private int goodsQty;

        public int getGoodsQty() {
            return goodsQty;
        }

        public void setGoodsQty(int goodsQty) {
            this.goodsQty = goodsQty;
        }

        public long getExpiresDate() {
            return expiresDate;
        }

        public void setExpiresDate(long expiresDate) {
            this.expiresDate = expiresDate;
        }

        public long getNowTimeStamp() {
            return nowTimeStamp;
        }

        public void setNowTimeStamp(long nowTimeStamp) {
            this.nowTimeStamp = nowTimeStamp;
        }

        public String getAttributeVal() {
            return attributeVal;
        }

        public void setAttributeVal(String attributeVal) {
            this.attributeVal = attributeVal;
        }

        public String getGoodName() {
            return goodName;
        }

        public void setGoodName(String goodName) {
            this.goodName = goodName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getGoodId() {
            return goodId;
        }

        public void setGoodId(int goodId) {
            this.goodId = goodId;
        }

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

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getGoodsImg() {
            return goodsImg;
        }

        public void setGoodsImg(String goodsImg) {
            this.goodsImg = goodsImg;
        }

        public String getValueId() {
            return valueId;
        }

        public void setValueId(String valueId) {
            this.valueId = valueId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public ShopAddressBean getAddress() {
            return address;
        }

        public void setAddress(ShopAddressBean address) {
            this.address = address;
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
            private String provinceName;
            private String cityId;
            private String areaId;
            private String areaName;
            private String detailAddress;
            private String userName;
            private String phone;
            private int isDefalt;
            private String cityName;

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
