package com.feitianzhu.huangliwo.core.network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.feitianzhu.huangliwo.GlobalUtil;
import com.feitianzhu.huangliwo.core.ApiErrorException;
import com.feitianzhu.huangliwo.core.log.HttpLogUtil;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetWorkState;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetworkConnectChangedReceiver;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.request.base.Request;
import com.lzy.okrx.adapter.ObservableBody;

import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by bch on 2020/5/11
 */
public abstract class BaseApiRequest extends AbsApiRequest {

    /**
     * 数据解析错误(已正常返回) json错误
     */
    public static final int kErrorTypeResponsePraseError = -100001;
    /**
     * 数据处理出错(外界对返回值的处理出现异常)
     */
    public static final int kErrorTypeResponseHandleError = -100002;
    /**
     * 网络连接不可用
     */
    public static final int kErrorTypeNoNetworkConnect = -100003;


    /**
     * 是否需要网络检测
     * 默认需要,
     */
    public boolean RequestCheckNetwork = true;

    /**
     * 是否展示 加载条
     */
    public boolean isShowLoading = false;

    /**
     * 网络请求的订阅对象
     */
    private Subscription sub;
    /**
     * 请求标记
     */
    public String requestTag;
    private ApiCallBack listener;

    @Override
    public void onStart() {
        HttpLogUtil.e(getAPIName(), "onStart");

        if (isShowLoading) {
            LoadingUtil.setLoadingViewShow(this, true);
        }
        if (listener != null && listener instanceof ApiLifeCallBack) {
            ((ApiLifeCallBack) listener).onStart();
        }
    }

    /**
     * 默认使用post请求
     *
     * @return
     */
    @Override
    public boolean usePost() {
        return true;
    }

    //Okgo 3.0 升级以后的方法

    /**
     * 确定是什么请求方式
     *
     * @param usePost
     * @return
     */
    private Request<String, ? extends Request> getRequest(boolean usePost) {
        HttpLogUtil.e(getAPIName(), "是否post" + usePost);

        if (usePost) {
            return OkGo.post(getApiUrl());
        } else {
            return OkGo.get(getApiUrl());
        }
    }

    /**
     * 设置请求策略参数
     *
     * @param req
     * @return
     */
    private Observable<String> getRequestObservable(Request<String, ?> req) {
        requestTag = this.hashCode() + "";
        req.tag(requestTag);
        Map<String, Object> build = appendParams(new ParamsBuilder()).build();
        req.params(ParamsBuilder.getHttpParams(build));
        req.converter(new StringConvert());
        //添加头参数
        req.headers(addHeads(OkGo.getInstance().getCommonHeaders()));
        HttpLogUtil.e(getAPIName(), "设置参数");

        return req.adapt(new ObservableBody<String>());
    }

    /**
     * 发起请求
     *
     * @param listener
     * @return
     */
    @Override
    public Subscription call(ApiCallBack listener) {
        this.listener = listener;
        NetWorkState networkStatus = NetworkConnectChangedReceiver.getNetworkStatus(GlobalUtil.getApplication());
        //检查网络
        if (RequestCheckNetwork && networkStatus == NetWorkState.NONE) {
            handleError(kErrorTypeNoNetworkConnect, "网络中断");
            return null;
        }
        try {
            //请求流程处理
            sub = Observable.just(usePost())                   //观察判断是否使用http post
                    .map(new Func1<Boolean, Request>() {
                        @Override
                        public Request call(Boolean aBoolean) {
                            //返回okgo request对象
                            return getRequest(aBoolean);
                        }
                    })
                    .flatMap(new Func1<Request, Observable<?>>() {
                        @Override
                        public Observable<?> call(Request request) {
                            //设置请求参数 https策略
                            return getRequestObservable(request);
                        }
                    })
                    .doOnSubscribe(() -> onStart())  //发起请求前 通知callback
                    .observeOn(Schedulers.io())                 //io线程
                    .map(new Func1<Object, BaseApiResponse>() {
                        @Override
                        public BaseApiResponse call(Object o) {
                            //解析json
                            return praseJson((String) o);
                        }
                    })
                    .map(baseRsq -> praseResponse(baseRsq))      //解析base对象（errorcode extra 字段）
                    .observeOn(AndroidSchedulers.mainThread())  //切换到主线程
                    .subscribe(new Subscriber<Object>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            handleError_private(e);

                        }

                        @Override
                        public void onNext(Object o) {
                            handleRsponse_private(o);

                        }
                    });
        } catch (Exception error) {
            handleError_private(error);
        }
        return sub;

    }

    private void handleRsponse_private(Object rsp) {

        rsp = handleRsponseAfterTransform(rsp);
        HttpLogUtil.e(getAPIName(), "成功");

        if (listener != null) {
            listener.onAPIResponse(rsp);
        }
        //请求结束
        onFinsh();
    }

    private void handleError_private(Throwable error) {
        error.printStackTrace();
        int errorCode;
        String errorMsg = "";
        if (error instanceof ApiErrorException) {
            //后台错误
            ApiErrorException ex = (ApiErrorException) error;
            errorCode = ex.getErrorCode();
            errorMsg = ex.getErrorMsg();
        } else if (error instanceof HttpException) {
            //http错误
            int code = ((HttpException) error).code();
            errorMsg = error.getMessage();
            errorCode = code;
        } else if (error instanceof JSONException) {
            //json解析出错
            errorCode = kErrorTypeResponsePraseError;
            errorMsg = "json解析错误";
        } else {
            //外界处理返回值错误
            errorCode = kErrorTypeResponseHandleError;
            errorMsg = "处理错误";
        }

        handleError(errorCode, errorMsg);
        //请求结束
        onFinsh();
    }

    /**
     * 解析json ,处理为公共数据模型
     *
     * @param json
     * @return
     */
    private BaseApiResponse praseJson(String json) {
        HttpLogUtil.e(getAPIName(), "解析公共model");

        BaseApiResponse baseApiResponse = (BaseApiResponse) JSON.parseObject(json, getResponseType());
        return baseApiResponse;
    }

    @Override
    public Object handleRsponseBeforeTransform(Object rsp) {
        HttpLogUtil.e(getAPIName(), "handleRsponseBeforeTransform");

        return rsp;
    }


    private Object praseResponse(BaseApiResponse baseRes) {
        HttpLogUtil.e(getAPIName(), "解析数据");

        if (!baseRes.isRequestSuccess()) {
            throw new ApiErrorException(baseRes);
        }
        Object rsp = baseRes.getData();
        if (rsp == null) {
            rsp = "";
        }
        rsp = handleRsponseBeforeTransform(rsp);
        if (rsp == null) {
            return "";
        } else {
            return JSON.parseObject(JSON.toJSONString(rsp), getDatatype());
        }
    }


    @Override
    public Object handleRsponseAfterTransform(Object rsp) {
        HttpLogUtil.e(getAPIName(), "handleRsponseAfterTransform");

        return rsp;
    }

    @Override
    public void handleError(int errorCode, String errorMsg) {
        HttpLogUtil.e("handleError    " + getAPIName(), errorCode + "::" + errorMsg);

        if (listener != null) {
            listener.onAPIError(errorCode, errorMsg);
        }
    }

    @Override
    public void onFinsh() {
        HttpLogUtil.e(getAPIName(), "onFinsh");

        if (isShowLoading) {
            LoadingUtil.setLoadingViewShow(this, false);
        }
        if (listener != null && listener instanceof ApiLifeCallBack) {
            ((ApiLifeCallBack) listener).onFinsh();
        }
    }

    @Override
    public void cancelRequest() {
        HttpLogUtil.e(getAPIName(), "cancelRequest");

        OkGo.getInstance().cancelTag(requestTag);
    }


}
