package com.feitianzhu.huangliwo.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.feitianzhu.huangliwo.common.Constant;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.vector.update_app.HttpManager;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import static com.feitianzhu.huangliwo.common.Constant.ACCESSTOKEN;
import static com.feitianzhu.huangliwo.common.Constant.TYPE;
import static com.feitianzhu.huangliwo.common.Constant.USERID;
import static java.lang.String.valueOf;

public class UpdateAppHttpUtil implements HttpManager {
    private String token;
    private String userId;

    public UpdateAppHttpUtil(Context context) {
        token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN);
        userId = SPUtils.getString(context, Constant.SP_LOGIN_USERID);
    }

    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        OkGo.<String>get(url)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .params(TYPE, "1")//
                .execute(new com.lzy.okgo.callback.StringCallback() {

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        callBack.onResponse(response.body());
                    }

                    @Override
                    public void onError(com.lzy.okgo.model.Response<String> response) {
                        super.onError(response);
                        callBack.onError(response.getException().getMessage());

                    }
                });
    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {

        OkGo.<String>post(url)
                .params(ACCESSTOKEN, token)//
                .params(USERID, userId)//
                .params(TYPE, "1")//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        callBack.onResponse(response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        callBack.onError(response.getException().getMessage());
                    }
                });
    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {

        OkGo.<File>get(url)
                .execute(new com.lzy.okgo.callback.FileCallback(path, fileName) {
                    @Override
                    public void onStart(com.lzy.okgo.request.base.Request<File, ? extends com.lzy.okgo.request.base.Request> request) {
                        super.onStart(request);
                        callback.onBefore();
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        callback.onResponse(response.body());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        callback.onProgress(progress.fraction, progress.totalSize);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        callback.onError(response.getException().getMessage());
                    }
                });



    }
}