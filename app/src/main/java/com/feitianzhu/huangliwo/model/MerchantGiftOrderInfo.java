package com.feitianzhu.huangliwo.model;

import java.io.Serializable;
import java.util.List;

public class MerchantGiftOrderInfo implements Serializable {
    public List<MerchantGiftOrderModel> all;
    public int waitUseCount;
    public List<MerchantGiftOrderModel> used;
    public int usedCount;
    public List<MerchantGiftOrderModel> waitUse;

}
