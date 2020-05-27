package com.feitianzhu.huangliwo.travel.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.http.MD5Utils;
import com.feitianzhu.huangliwo.travel.base.OilTimeBaseRequest;

/**
 * Created by bch on 2020/5/20
 */
public class OilTimeRequest extends OilTimeBaseRequest {
    public String platformId;
    public String phone;
    public String timestamp;
    public String app_key;
    private String sign;


    @Override
    public String getAPIName() {
        return "services/v3/begin/getSecretCode";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("platformId", platformId)
                .append("app_key", app_key)
                .append("sign", getSign())
                .append("phone", phone)
                .append("timestamp", timestamp)
        );
    }

    public String getSign() {
        String s = "7ebab712f4d320a5e035a6653b767cef" + "app_key" + app_key + "phone" + phone + "platformId" + platformId + "timestamp" + timestamp
                + "7ebab712f4d320a5e035a6653b767cef";
        String encode = MD5Utils.encode(s);
        return encode;
    }


    @Override
    public TypeReference getDatatype() {
        return new TypeReference<String>() {
        };
    }
}
