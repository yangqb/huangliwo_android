package com.feitianzhu.huangliwo.im.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.model.MineInfoModel;

public class OtherUserInfoRequest extends BaseRequest {

    public String userId;

    @Override
    public String getAPIName() {
//        return "fhwl/user/getuserinfo";
        return "fhwl/user/getUserInfo";
    }

    @Override
    public boolean usePost() {
        return false;
    }

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append(Constant.USERID, userId));
    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<MineInfoModel>() {
        };
    }
}
