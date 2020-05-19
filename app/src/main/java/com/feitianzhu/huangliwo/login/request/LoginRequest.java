package com.feitianzhu.huangliwo.login.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.login.model.UserInfo;

public class LoginRequest extends BaseRequest {

    public String phone;
    public String password;

    @Override
    public String getAPIName() {
        return "fhwl/commons/account/login";
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("phone", phone)
                .append("password", password));
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<UserInfo>() {

        };
    }
}
