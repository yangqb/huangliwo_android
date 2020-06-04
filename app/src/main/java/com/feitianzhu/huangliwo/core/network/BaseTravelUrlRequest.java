package com.feitianzhu.huangliwo.core.network;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.BaseResponse;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;

/**
 * Created by bch on 2020/5/20
 * 为什么需要这个基类,
 * 切换域名

 */
public abstract class BaseTravelUrlRequest extends BaseRequest {
    /**
     * 设置域名 端口
     *
     * @return
     */
    @Override
    public String getAPIBaseURL() {
//        return "http://192.168.0.142:8087/";
        return Urls.TICKET_BASE_URL;
    }

    /**
     * 设置base返回基类,进行统一处理
     *
     * @return
     */
    @Override
    public TypeReference getResponseType() {
        return new TypeReference<BaseResponse>() {
        };
    }

    @Override
    public void handleError(int errorCode, String errorMsg) {
        super.handleError(errorCode, errorMsg);

    }
}
