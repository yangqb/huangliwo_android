package com.feitianzhu.huangliwo.core.network;

import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.feitianzhu.huangliwo.GlobalUtil;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.settings.ChangeLoginPassword;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.Urls;
import com.hjq.toast.ToastUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.ConfirmPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;

/**
 * Created by bch on 2020/5/11
 * 这个base请求基类为通用基类请求
 */
public abstract class BaseRequest extends BaseApiRequest {


    private ConfirmPopupView confirmPopupView;

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
            Log.e("TAG11", "handleError: " );
//            登录异常被踢
            boolean loginDialog = SPUtils.getBoolean(GlobalUtil.getCurrentActivity(), Constant.LOGIN_DIALOG);
            String token = SPUtils.getString(GlobalUtil.getCurrentActivity(), Constant.SP_ACCESS_TOKEN, "");
            if (confirmPopupView == null) {
                confirmPopupView = new XPopup.Builder(GlobalUtil.getCurrentActivity())
                        .autoDismiss(false)
                        .dismissOnTouchOutside(false)
                        .enableDrag(false)
                        .asConfirm("", "您的账号已在其他设备登陆，如果这不是您的操作，请及时修改密码并重新登陆。", "重新登录", "找回密码", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                //context.startActivity(new Intent(context, ForgetPasswordActivity.class));
                                Intent intent = new Intent(GlobalUtil.getCurrentActivity(), ChangeLoginPassword.class);
                                GlobalUtil.getCurrentActivity().startActivity(intent);
                            }
                        }, new OnCancelListener() {
                            @Override
                            public void onCancel() {
                                SPUtils.putString(GlobalUtil.getCurrentActivity(), Constant.SP_PASSWORD, "");
                                SPUtils.putString(GlobalUtil.getCurrentActivity(), Constant.SP_LOGIN_USERID, "");
                                SPUtils.putString(GlobalUtil.getCurrentActivity(), Constant.SP_ACCESS_TOKEN, "");
                                Constant.ACCESS_TOKEN = "";
                                Constant.LOGIN_USERID = "";
                                Constant.PHONE = "";
                                Intent intent = new Intent(GlobalUtil.getCurrentActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                GlobalUtil.getCurrentActivity().startActivity(intent);
                            }
                        }, false)
                        .bindLayout(R.layout.layout_dialog_login);
                confirmPopupView.show();//绑定已有布局
            }
            SPUtils.putBoolean(GlobalUtil.getCurrentActivity(), Constant.LOGIN_DIALOG, false);
        } else if (errorCode == 404) {
            ToastUtils.show("数据找不到");
            //找不到
        } else if (errorCode == kErrorTypeNoNetworkConnect) {
            ToastUtils.show("网络不可用");

            //网络不可用
        } else if (errorCode == kErrorTypeResponseHandleError) {
            ToastUtils.show("数据处理错误");

            //外部数据处理错误
        } else if (errorCode == kErrorTypeResponsePraseError) {
            ToastUtils.show("解析错误");

            //json解析错误
        } else {
            ToastUtils.show("网络错误,请重试");
        }
    }
}

