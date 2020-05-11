package com.feitianzhu.huangliwo.core.network.test;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.BaseResponse;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.model.UpdateAppModel;
import com.lzy.okgo.model.HttpHeaders;

import static com.feitianzhu.huangliwo.common.Constant.UAPDATE;

/**
 * Created by bch on 2020/5/11
 */
public class UpdataRequest extends BaseRequest {

    @Override
    public String getAPIName() {
        return UAPDATE;
    }

    @Override
    public boolean usePost() {
        return false;
    }
    //    http://8.129.218.83:8088/fhwl/soft/newv?accessToken=5fde8fdbb42c406b96d06b9a7c3e86e1&userId=321276&type=1

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return super.appendParams(builder.append("accessToken", "5fde8fdbb42c406b96d06b9a7c3e86e1")
                .append("userId", "321276")
                .append("type", "1"));
    }

    @Override
    public HttpHeaders addHeads(HttpHeaders headers) {
        return null;
    }

    @Override
    public boolean getshowSuccessToast() {
        return false;
    }

    @Override
    public boolean getshowFailToast() {
        return false;
    }

    @Override
    public void showToast(boolean success, int code, String message) {

    }

    @Override
    public TypeReference getDatatype() {
        return new TypeReference<UpdateAppModel>() {
        };
    }
}
