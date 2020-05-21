package com.feitianzhu.huangliwo.travel.base;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.ParamsBuilder;
import com.feitianzhu.huangliwo.travel.base.BaseTravelRequest;
import com.feitianzhu.huangliwo.travel.request.OilTimeResponse;
import com.hjq.toast.ToastUtils;

/**
 * Created by bch on 2020/5/20
 */
public abstract class OilTimeBaseRequest extends BaseRequest {

    @Override
    public String getAPIBaseURL() {
        return "http://test-mcs.czb365.com/";
    }


    @Override
    public TypeReference getResponseType() {
        return new TypeReference<OilTimeResponse>() {
        };
    }

    /**
     * 在这里处理错误情况
     * 情况有 网络错误码
     * 服务器业务逻辑错误代码
     *
     * @param errorCode
     * @param errorMsg
     */
    @Override
    public void handleError(int errorCode, String errorMsg) {
//        super.handleError(errorCode, errorMsg);

        ToastUtils.show(errorMsg);
    }

}
