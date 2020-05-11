package com.feitianzhu.huangliwo.core.network;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.GlobalUtil;
import com.feitianzhu.huangliwo.core.ApiErrorException;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetWorkState;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetworkConnectChangedReceiver;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.lzy.okrx.adapter.ObservableBody;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.feitianzhu.huangliwo.core.network.networkcheck.NetWorkState.NONE;

/**
 * Created by bch on 2020/5/11
 */
public abstract class BaseApiRequest extends AbsApiRequest implements AbsApiRequestLife {

    /**
     * http错误(后台未收到请求)
     */
    public static final int kErrorTypeHttpError = 0;
    /**
     * 数据解析错误(已正常返回)
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
     * 网络超时设置
     */
    public int DEFAULT_MILLISECONDS = 0;

    /**
     * 是否需要网络检测
     * 默认需要,
     */
    public boolean RequestCheckNetwork = true;
    /**
     * 网络请求的订阅对象
     */
    private Subscription sub;
    private String requestTag;

    @Override
    public String getAPIBaseURL() {
        return null;
    }

    @Override
    public abstract String getAPIName();

    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return builder;
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

    @Override
    public TypeReference getResponseType() {
        return null;
    }

    @Override
    public TypeReference getDatatype() {
        return null;
    }

    @Override
    public Subscription call(final ApiCallBack listener) {

        NetWorkState networkStatus = NetworkConnectChangedReceiver.getNetworkStatus(GlobalUtil.getApplication());
        //检查网络
        if (RequestCheckNetwork && networkStatus == NetWorkState.NONE) {
            if (listener != null) {
                listener.onAPIError(kErrorTypeNoNetworkConnect, getFailToast());
            }
            handleError(listener, kErrorTypeNoNetworkConnect, getFailToast());
            if (showFailToast) {
                showToast(false, kErrorTypeNoNetworkConnect, getFailToast());
            }
//            retry(listener);
            return null;
        }
//        if (showLoadingView) {
//            setLoadingViewShow(true);
//        }
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
                    .doOnSubscribe(() -> requestWillStart(listener))  //发起请求前 通知callback
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
                            handleError_private(listener, e);

                        }

                        @Override
                        public void onNext(Object o) {
                            handleRsponse_private(listener, o);

                        }
                    });
        } catch (Exception error) {
            handleError_private(listener, error);
        }
        return sub;

    }

    private void handleRsponse_private(ApiCallBack listener, Object rsp) {
        //请求成功保存日志
//
//        if (TestModuleManager.getRequestLogSwitch()) {
//            SaveForGowithmi saveForGowithmi = new SaveForGowithmi("request" + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000));
//            saveForGowithmi.suffix = ".log";
//            saveForGowithmi.saveString(requestTag + "\n" + "Success");
//        }
        rsp = handleRsponseAfterTransform(rsp);
//        if (showSuccessToast) {
//            showToast(true, 0, getSuccessToast());
//        }
        if (listener != null) {
            listener.onAPIResponse(rsp);
        }
//        if (showLoadingView) {
//            setLoadingViewShow(false);
//        }
    }

    private void handleError_private(ApiCallBack listener, Throwable error) {
        error.printStackTrace();
        int errorCode;
        String errorMsg = getFailToast();
        //请求失败保存日志
//        if (TestModuleManager.getRequestLogSwitch()) {
//            SaveForGowithmi saveForGowithmi = new SaveForGowithmi("request"
//                    + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000));
//            saveForGowithmi.suffix = ".log";
//            saveForGowithmi.saveString(requestTag + "..." + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000)
//                    + "\n" + error.getMessage());
//        }

        if (error instanceof ApiErrorException) {
            //后台错误
            ApiErrorException ex = (ApiErrorException) error;
            errorCode = ex.getErrorCode();
            errorMsg = ex.getErrorMsg();
        } else if (error instanceof HttpException) {
            //http错误
            errorCode = kErrorTypeHttpError;
        } else if (error instanceof JSONException) {
            //json解析出错
            errorCode = kErrorTypeResponsePraseError;
        } else {
            //外界处理返回值错误
            errorCode = kErrorTypeResponseHandleError;
        }

        if (listener != null) {
            listener.onAPIError(errorCode, errorMsg);
        }
        handleError(listener, errorCode, errorMsg);
        if (showFailToast) {
            showToast(false, errorCode, errorMsg);
        }
//        retry(listener);

//        if (showLoadingView) {
//            setLoadingViewShow(false);
//        }
    }

    private Object praseResponse(BaseApiResponse baseRes) {
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
            String jsonString = "";
            if (baseRes.getSupportJson()) {
                jsonString = (String) rsp;
            } else {
                jsonString = JSON.toJSONString(rsp);
            }
            return JSON.parseObject(jsonString, getDatatype());
        }
    }

    private BaseApiResponse praseJson(String json) {
        BaseApiResponse baseApiResponse = (BaseApiResponse) JSON.parseObject(json, getResponseType());
        if (baseApiResponse.getSupportJson()) {
            baseApiResponse.setJsonData(json);
        }
        return baseApiResponse;
    }

    private void requestWillStart(ApiCallBack listener) {
    }

    //Okgo 3.0 升级以后的方法
    private Request<String, ? extends Request> getRequest(boolean usePost) {
        if (DEFAULT_MILLISECONDS <= 5000) {
            DEFAULT_MILLISECONDS = 1000 * 20;
        }
        OkHttpClient.Builder builder = OkGo.getInstance().getOkHttpClient().newBuilder();
        //全局的读取超时时间
        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        OkGo.getInstance().setOkHttpClient(builder.build());
        if (usePost) {
            return OkGo.post(getApiUrl());
        } else {
            return OkGo.get(getApiUrl());
        }
    }

    private Observable<String> getRequestObservable(Request<String, ?> req) {
        int i = OkGo.getInstance().getOkHttpClient().readTimeoutMillis();
        Log.e("TAG", "getRequestObservable: " + i);
        requestTag = this.hashCode() + "";
        req.tag(requestTag);

        Map<String, Object> build = appendParams(appendBaseParams(new ParamsBuilder())).build();

        String params = "";
        for (String key : build.keySet()) {
            Object o = build.get(key);
            params += key + ":" + o + "\n";
        }
        //开始请求保存日志
//        if (TestModuleManager.getRequestLogSwitch()) {
//            SaveForGowithmi saveForGowithmi = new SaveForGowithmi("request" + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000));
//            saveForGowithmi.suffix = ".log";
//            saveForGowithmi.saveString(requestTag + "\n" + getApiUrl() + "\n" + params);
//        }
//        if (requestUseBody && req instanceof PostRequest) {
//            ((PostRequest) req).upRequestBody(RequestBody.create(JSONBody, JSON.toJSON(build).toString()));
//        } else {
        req.params(ParamsBuilder.getHttpParams(build));
//        }
        req.converter(new StringConvert());
        req.headers(addHeads(OkGo.getInstance().getCommonHeaders()));
        return req.adapt(new ObservableBody<String>());
    }

    @Override
    public void handleError(ApiCallBack listener, int errorCode, String errorMsg) {

    }

    @Override
    public void cancelRequest() {

    }

    @Override
    public Object handleRsponseBeforeTransform(Object rsp) {
        return null;
    }

    @Override
    public Object handleRsponseAfterTransform(Object rsp) {
        return null;
    }

    @Override
    public String getSuccessToast() {
        return null;
    }

    @Override
    public String getFailToast() {
        return null;
    }
}
