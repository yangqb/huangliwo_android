package com.feitianzhu.huangliwo.im.request;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.BaseTravelRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.im.bean.ConverzServiceListBean;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.StringUtils;

import java.util.List;

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
