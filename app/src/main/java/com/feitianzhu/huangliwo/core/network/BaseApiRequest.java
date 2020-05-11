package com.feitianzhu.huangliwo.core.network;

import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.gowithmi.mapworld.app.GlobalUtil;
import com.gowithmi.mapworld.app.test.TestModuleManager;
import com.gowithmi.mapworld.core.util.DateUtil;
import com.gowithmi.mapworld.core.util.NetworkStatusUtil;
import com.gowithmi.mapworld.core.util.SaveForGowithmi;
import com.gowithmi.mapworld.core.util.SignatureUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.lzy.okrx.adapter.ObservableBody;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.gowithmi.mapworld.app.api.base.NetWorkConfig.addHeaders;


/**
 * Created by gundam on 16/11/2.
 * <p>
 * errorCode:
 * 0:http错误(后台未收到请求)
 * -100001:数据解析错误(已正常返回)
 * -100002:数据处理出错(外界对返回值的处理出现异常)
 * -100003:网络连接不可用
 * other:后台错误(后台已经收到了请求)
 */

public abstract class BaseApiRequest extends AbsApiRequest {
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
    private Subscription sub;
    private String requestTag;

    /**
     * 是否转菊花
     */
    public boolean showLoadingView = false;

    public boolean requestUseGet = false;

    public boolean requestUseBody = false;
    //是否检查网络
    public boolean requestCheckNetwork = true;

    /**
     * 是否显示成功的toast
     */
    public boolean showSuccessToast = false;

    /**
     * 是否显示失败的toast
     */
    public boolean showFailToast = true;

    /**
     * 失败重试次数,默认不重试
     */
    protected int retryCount = 0;

    /**
     * 失败重试间隔,默认10秒
     */
    protected int retryInterval = 10;

    public static final MediaType JSONBody = MediaType.parse("application/json");
    public int DEFAULT_MILLISECONDS = 0;

    //===================== override if need ========================

    @Override
    public String getAPIBaseURL() {
        return "";
    }

    /**
     * 拼接公共参数
     */
    protected ParamsBuilder appendBaseParams(ParamsBuilder builder) {
        return builder;
    }

    /**
     * 拼接其他参数,若出现多级继承关系,请记得调用super
     */
    @Override
    public ParamsBuilder appendParams(ParamsBuilder builder) {
        return builder;
    }

    /**
     * 成功提示
     */
    protected String getSuccessToast() {
        return "";
    }

    /**
     * 失败提示
     */
    protected String getFailToast() {
        return "";
    }

    /**
     * 若要转菊花,重写此方法
     */
    protected void setLoadingViewShow(Boolean show) {

    }

    @Override
    public TypeReference getResponseType() {
        return new TypeReference<BaseApiResponse>() {
        };
    }

    protected boolean usePost() {
        return !requestUseGet;
    }

    /**
     * 在将返回值转换成对应的model之前,对返回值进行一些处理
     */
    protected Object handleRsponseBeforeTransform(Object rsp) {
        return rsp;
    }

    /**
     * 在将返回值转换成对应的model后,对返回值进行一些处理
     */
    protected Object handleRsponseAfterTransform(Object rsp) {
        return rsp;
    }

    /**
     * 处理错误
     */
    protected void handleError(ApiCallBack listener, int errorCode, String errorMsg) {
    }

    /**
     * 在此方法里弹出toast提示
     */
    protected void showToast(boolean success, int code, String message) {

    }

    //===================== public method ========================

    @Override
    public Subscription call(final ApiCallBack listener) {
        //检查网络
        if (requestCheckNetwork && !NetworkStatusUtil.isNetworkConnected(GlobalUtil.getApplication())) {
            if (listener != null) {
                listener.onAPIError(kErrorTypeNoNetworkConnect, getFailToast());
            }
            handleError(listener, kErrorTypeNoNetworkConnect, getFailToast());
            if (showFailToast) {
                showToast(false, kErrorTypeNoNetworkConnect, getFailToast());
            }
            retry(listener);
            return null;
        }
        if (showLoadingView) {
            setLoadingViewShow(true);
        }
        try {
            //请求流程处理
            sub = Observable.just(usePost())                   //观察判断是否使用http post
                    .map(usePost -> getRequest(usePost))              //返回okgo request对象
                    .flatMap(req -> getRequestObservable(req))             //设置请求参数 https策略
                    .doOnSubscribe(() -> requestWillStart(listener))  //发起请求前 通知callback
                    .observeOn(Schedulers.io())                 //io线程
                    .map(resJson -> praseJson(resJson))         //解析json
                    .map(baseRsq -> praseResponse(baseRsq))      //解析base对象（errorcode extra 字段）
                    .observeOn(AndroidSchedulers.mainThread())  //切换到主线程
                    .subscribe(res -> {
                                handleRsponse_private(listener, res);
                            },//通知callback返回对象
                            error -> {
                                handleError_private(listener, error);
                            }//处理异常
                    );

        } catch (Exception error) {
            handleError_private(listener, error);
        }
        return sub;
    }

    @Override
    public void cancelRequest() {
        OkGo.getInstance().cancelTag(requestTag);
    }

    /**
     * 添加头参数
     *
     * @return
     */
    public HttpHeaders addHeads(HttpHeaders headers) {
        return addHeaders(headers);
    }

    //===================== public method ========================


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
        if (TestModuleManager.getRequestLogSwitch()) {
            SaveForGowithmi saveForGowithmi = new SaveForGowithmi("request" + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000));
            saveForGowithmi.suffix = ".log";
            saveForGowithmi.saveString(requestTag + "\n" + getApiUrl() + "\n" + params);
        }
        if (requestUseBody && req instanceof PostRequest) {
            ((PostRequest) req).upRequestBody(RequestBody.create(JSONBody, JSON.toJSON(build).toString()));
        } else {
            req.params(SignatureUtil.getMap(build));
        }
        req.converter(new StringConvert());
        req.headers(addHeads(OkGo.getInstance().getCommonHeaders()));
        return req.adapt(new ObservableBody<String>());
    }

    private void requestWillStart(ApiCallBack listener) {
    }

    private BaseApiResponse praseJson(String json) {
        BaseApiResponse baseApiResponse = (BaseApiResponse) JSON.parseObject(json, getResponseType());
        if (baseApiResponse.getSupportJson()) {
            baseApiResponse.setJsonData(json);
        }
        return baseApiResponse;
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

    private void handleRsponse_private(ApiCallBack listener, Object rsp) {
        //请求成功保存日志

        if (TestModuleManager.getRequestLogSwitch()) {
            SaveForGowithmi saveForGowithmi = new SaveForGowithmi("request" + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000));
            saveForGowithmi.suffix = ".log";
            saveForGowithmi.saveString(requestTag + "\n" + "Success");
        }
        rsp = handleRsponseAfterTransform(rsp);
        if (showSuccessToast) {
            showToast(true, 0, getSuccessToast());
        }
        if (listener != null) {
            listener.onAPIResponse(rsp);
        }
        if (showLoadingView) {
            setLoadingViewShow(false);
        }
    }

    private void handleError_private(ApiCallBack listener, Throwable error) {
        error.printStackTrace();
        int errorCode;
        String errorMsg = getFailToast();
        //请求失败保存日志
        if (TestModuleManager.getRequestLogSwitch()) {
            SaveForGowithmi saveForGowithmi = new SaveForGowithmi("request"
                    + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000));
            saveForGowithmi.suffix = ".log";
            saveForGowithmi.saveString(requestTag + "..." + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000)
                    + "\n" + error.getMessage());
        }

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
        retry(listener);

        if (showLoadingView) {
            setLoadingViewShow(false);
        }
    }

    //失败重试
    private void retry(ApiCallBack listener) {
        if (retryCount > 0 && retryInterval >= 0) {
            retryCount--;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    call(listener);
                }
            }, retryInterval * 1000);
        }
    }

    private String getApiUrl() {
        return getAPIBaseURL() + getAPIName();
    }

}
