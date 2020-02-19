package com.feitianzhu.huangliwo.model;

import java.io.Serializable;

public class MerchantGitModel implements Serializable {
    public String createTime;// (string, optional): 创建时间 ,
    public String isDel;// (integer, optional): 是否删除 0否 1是 ,
    public String id;
    //@ApiModelProperty("商户ID")
    public int merchantId;

    //@ApiModelProperty("赠品名称")
    public String giftName;
    //@ApiModelProperty("赠品价格")
    public double price;

    //@ApiModelProperty("描述")
    public String desc;

}
