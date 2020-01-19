package com.feitianzhu.huangliwo.pushshop.bean;

import java.io.Serializable;

/**
 * package name: com.feitianzhu.huangliwo.pushshop.bean
 * user: yangqinbo
 * date: 2020/1/18
 * time: 12:42
 * email: 694125155@qq.com
 */
public class MerchantsPaymentCodeModel implements Serializable {

    /**
     * qrCodeUrl : http://39.106.65.35:8089/fhwl/invest?merchantId=30&merchantLogo=http://39.106.65.35:8089/user/merchant/2020/01/13/5cc92a8b07234194939888734aa2d3c9.png&noncestr=0hx04443y85wilfc18bj9xjuccpetn2l&merchantName=海底捞&timestamp=1579322469932&&sign=e773c9091b71748ab4d2af5875a800c8
     * merchantName : 海底捞
     * merchantHeadImg : http://39.106.65.35:8089/user/merchant/2020/01/13/5cc92a8b07234194939888734aa2d3c9.png
     */

    private String qrCodeUrl;
    private String merchantName;
    private String merchantHeadImg;

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantHeadImg() {
        return merchantHeadImg;
    }

    public void setMerchantHeadImg(String merchantHeadImg) {
        this.merchantHeadImg = merchantHeadImg;
    }
}
