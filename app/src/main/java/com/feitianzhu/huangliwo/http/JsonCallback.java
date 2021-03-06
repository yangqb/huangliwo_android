/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feitianzhu.huangliwo.http;

import android.content.Context;
import android.content.Intent;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.settings.ChangeLoginPassword;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：
 * ================================================
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
//        request.headers("header1", "HeaderValue1")
//                .params("params1", "ParamsValue1")
//                .params("token", "3215sdf13ad1f65asd4f3ads1f");
    }

    @Override
    public T convertResponse(okhttp3.Response response) {

        ResponseBody body = response.body();
        if (body == null) return null;
        T data = null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if (type != null) {
            data = Convert.fromJson(jsonReader, type);
        } else if (clazz != null) {
            data = gson.fromJson(jsonReader, clazz);
        } else {
            Type genType = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            data = Convert.fromJson(jsonReader, type);
        }
        return data;


        /*Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        if(!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
        Type rawType = ((ParameterizedType)type).getRawType();
        Type typeArgument = ((ParameterizedType)type).getActualTypeArguments()[0];

        ResponseBody body = response.body();

        if (body == null) {
            return null;
        }
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if(rawType != LzyResponse.class){
            T data = Convert.fromJson(jsonReader,type);
            response.close();
            return data;
        }else {
            if(typeArgument == Void.class){
                SimpleResponse simpleResponse = Convert.fromJson(jsonReader,SimpleResponse.class);
                response.close();

                return (T)simpleResponse.toLzyResponse();
            }else {
                LzyResponse lzyResponse = Convert.fromJson(jsonReader,LzyResponse.class);
                response.close();
                int code = lzyResponse.code;
                if(code == 0){
                    return (T)lzyResponse;
                }else {
                    throw new IllegalStateException("bbbbb");
                }
            }
        }*/
    }

    private ConfirmPopupView confirmPopupView;

    public void onSuccess(Context context, String string, int errorCode) {
        if (errorCode != 0) {
            if (errorCode == 100021105) {
                boolean loginDialog = SPUtils.getBoolean(context, Constant.LOGIN_DIALOG);
                String token = SPUtils.getString(context, Constant.SP_ACCESS_TOKEN, "");
                    if (confirmPopupView == null) {
                        confirmPopupView = new XPopup.Builder(context)
                                .autoDismiss(false)
                                .dismissOnTouchOutside(false)
                                .enableDrag(false)
                                .asConfirm("", "您的账号已在其他设备登陆，如果这不是您的操作，请及时修改密码并重新登陆。", "重新登录", "找回密码", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        //context.startActivity(new Intent(context, ForgetPasswordActivity.class));
                                        Intent intent = new Intent(context, ChangeLoginPassword.class);
                                        context.startActivity(intent);
                                    }
                                }, new OnCancelListener() {
                                    @Override
                                    public void onCancel() {
                                        SPUtils.putString(context, Constant.SP_PASSWORD, "");
                                        SPUtils.putString(context, Constant.SP_LOGIN_USERID, "");
                                        SPUtils.putString(context, Constant.SP_ACCESS_TOKEN, "");
                                        Constant.ACCESS_TOKEN = "";
                                        Constant.LOGIN_USERID = "";
                                        Constant.PHONE = "";
                                        Intent intent = new Intent(context, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                }, false)
                                .bindLayout(R.layout.layout_dialog_login);
                        confirmPopupView.show();//绑定已有布局
                    }
                    SPUtils.putBoolean(context, Constant.LOGIN_DIALOG, false);


            } else if (errorCode == 100010100) {
                SPUtils.putString(context, Constant.SP_PASSWORD, "");
                SPUtils.putString(context, Constant.SP_LOGIN_USERID, "");
                SPUtils.putString(context, Constant.SP_ACCESS_TOKEN, "");
                Constant.ACCESS_TOKEN = "";
                Constant.LOGIN_USERID = "";
                Constant.PHONE = "";
                Intent intent = new Intent(context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                ToastUtils.show(string == null ? "服务器错误" : string);
            }
        }
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        super.onError(response);
        int code = response.code();
        if (response.getException() instanceof ConnectException) {
            ToastUtils.show("网络连接请求失败");
        } else if (response.getException() instanceof SocketTimeoutException) {
            ToastUtils.show("请求超时，请检查网络");
        } else if (response.getException() instanceof SocketException) {
            ToastUtils.show("服务器异常");
        } else {
            //ToastUtils.show(response.message());
        }
    }
}
