package com.feitianzhu.huangliwo.core.network;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.utils.Urls;

/**
 * Created by bch on 2020/5/11
 * 这个base请求基类为通用基类请求
 */
public abstract class BaseRequest extends BaseApiRequest {


    /**
     * 设置域名 端口
     *
     * @return
     */
    @Override
    public String getAPIBaseURL() {
        return Urls.BASE_URL;
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
        super.handleError(errorCode, errorMsg);

        if (errorCode == 100021105) {
//            登录异常被踢
        } else if (errorCode == 404) {
            //找不到
        } else if (errorCode == kErrorTypeNoNetworkConnect) {
            //网络不可用
        } else if (errorCode == kErrorTypeResponseHandleError) {
            //外部数据处理错误
        } else if (errorCode == kErrorTypeResponsePraseError) {
            //json解析错误
        }
    }
}

