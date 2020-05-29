package com.feitianzhu.huangliwo.travel.base;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.core.network.BaseRequest;
import com.feitianzhu.huangliwo.core.network.BaseResponse;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;

/**
 * Created by bch on 2020/5/20
 * 为什么需要这个基类,
 * 切换域名
 * 用这个基类的几个请求都是对接团油的接口,出问题的几率非常大,最好吧服务器返回的message展示出来
 */
public abstract class BaseTravelRequest extends BaseRequest {
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
