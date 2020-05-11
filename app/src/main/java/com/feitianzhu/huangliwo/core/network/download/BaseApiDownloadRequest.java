package com.feitianzhu.huangliwo.core.network.download;

import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.gowithmi.mapworld.app.GlobalUtil;
import com.gowithmi.mapworld.app.test.TestModuleManager;
import com.gowithmi.mapworld.core.network.ApiErrorException;
import com.gowithmi.mapworld.core.util.DateUtil;
import com.gowithmi.mapworld.core.util.NetworkStatusUtil;
import com.gowithmi.mapworld.core.util.SaveForGowithmi;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

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

public abstract class BaseApiDownloadRequest extends AbsApiDownloadRequest {
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
    public static final int kErrorTypeNoNetdownload = -10000122;
    private String requestTag;

    /**
     * 是否转菊花
     */
    public boolean showLoadingView = false;

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

    public int DEFAULT_MILLISECONDS = 0;

    private DownloadTask request1;


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

    /**
     * 处理错误
     */
    protected void handleError(ApiCallBackDownload listener, int errorCode, String errorMsg) {
    }

    /**
     * 在此方法里弹出toast提示
     */
    protected void showToast(boolean success, int code, String message) {

    }

    //===================== public method ========================
    public String getFileName() {
        return System.currentTimeMillis() + ".download";
    }

    public String getFileDir() {
        return "/sdcard/GoWithMi/download";

    }

    @Override
    public DownloadTask call(@Nullable final ApiCallBackDownload listener) {

        //检查网络
        if (requestCheckNetwork && !NetworkStatusUtil.isNetworkConnected(GlobalUtil.getApplication())) {
            if (listener != null) {
                listener.onAPIError(kErrorTypeNoNetworkConnect, getFailToast(), new Progress());
            }
            handleError(listener, kErrorTypeNoNetworkConnect, getFailToast());
            if (showFailToast) {
                showToast(false, kErrorTypeNoNetworkConnect, getFailToast());
            }
            return null;
        }
        if (showLoadingView) {
            setLoadingViewShow(true);
        }
//        if (DEFAULT_MILLISECONDS <= 5000) {
//            DEFAULT_MILLISECONDS = 1000 * 20;
//        }
        OkHttpClient.Builder builder = OkGo.getInstance().getOkHttpClient().newBuilder();
//        //全局的读取超时时间
//        builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//        //全局的写入超时时间
//        builder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//        //全局的连接超时时间
        builder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        OkGo.getInstance().setOkHttpClient(builder.build());
        PostRequest<File> request = OkGo.<File>post(getApiUrl());
        requestTag = getFileName();
        request.tag(requestTag);

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
        int i = OkGo.getInstance().getOkHttpClient().readTimeoutMillis();

        Log.e("TAG", "getRequestObservable: " + i);

        request.headers(addHeads(OkGo.getInstance().getCommonHeaders()));
        request1 = OkDownload.request(requestTag, request);
        request1.folder(getFileDir());
        request1.fileName(getFileName());

        try {
            request1.save();
            request1.register(new DownloadListener(requestTag) {
                @Override
                public void onStart(Progress progress) {
                    //                   正在下载中
                    listener.onStart(progress);
                }

                @Override
                public void onProgress(Progress progress) {
//                  下载进度
                    request1.save();

                    listener.onProgress(progress);

                }

                @Override
                public void onError(Progress progress) {
//                  下载出错
                    handleError_private(listener, new ApiDownloadErrorException(0, "下载出错"), progress);

                }

                @Override
                public void onFinish(File file, Progress progress) {
//             下载完成
                    handleRsponse_private(listener, file, progress);
                }

                @Override
                public void onRemove(Progress progress) {
//取消下载
                    listener.onRemove(progress);

                }
            });
//            request1.start();
            return request1;
        } catch (Exception error) {
            handleError_private(listener, error, new Progress());
        }
        return null;
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


    private void handleRsponse_private(ApiCallBackDownload listener, File file, Progress progress) {
        //请求成功保存日志

        if (TestModuleManager.getRequestLogSwitch()) {
            SaveForGowithmi saveForGowithmi = new SaveForGowithmi("request" + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000));
            saveForGowithmi.suffix = ".log";
            saveForGowithmi.saveString(requestTag + "\n" + "Success");
        }
        if (showSuccessToast) {
            showToast(true, 0, getSuccessToast());
        }
        if (listener != null) {
            listener.onFinish(file, progress);
        }
        if (showLoadingView) {
            setLoadingViewShow(false);
        }
    }

    private void handleError_private(ApiCallBackDownload listener, Throwable error, Progress progress) {
        error.printStackTrace();
        int errorCode;
        String errorMsg = getFailToast();
        //请求失败保存日志
        if (TestModuleManager.getRequestLogSwitch()) {
            SaveForGowithmi saveForGowithmi = new SaveForGowithmi("requestdownload" + DateUtil.formatDateYYYYMMDD(System.currentTimeMillis() / 1000));
            saveForGowithmi.suffix = ".log";
            saveForGowithmi.saveString(requestTag + "\n" + error.getMessage());
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
            listener.onAPIError(errorCode, errorMsg, progress);
        }
        handleError(listener, errorCode, errorMsg);
        if (showFailToast) {
            showToast(false, errorCode, errorMsg);
        }
        if (showLoadingView) {
            setLoadingViewShow(false);
        }
    }


    private String getApiUrl() {
        return getAPIBaseURL() + getAPIName();
    }

}
