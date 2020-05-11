package com.feitianzhu.huangliwo.core.network.download;

import com.gowithmi.mapworld.R;
import com.gowithmi.mapworld.app.GlobalUtil;
import com.gowithmi.mapworld.app.LoadingUtil;
import com.gowithmi.mapworld.app.RxCodeConstants;
import com.gowithmi.mapworld.app.Toaster;
import com.gowithmi.mapworld.app.api.base.AppUrlConfig;
import com.gowithmi.mapworld.core.rxbus.RxBus;

public abstract class BasedownloadRequest extends BaseApiDownloadRequest {

    @Override
    public String getAPIBaseURL() {
        return AppUrlConfig.getAPIBaseUrl();
    }



    @Override
    protected void handleError(ApiCallBackDownload listener, int errorCode, String errorMsg) {
        if (errorCode == 3001) {
            //登陆异常
            RxBus.getDefault().post(RxCodeConstants.NETWORK_TOKEN_EXPIRATION, true);
            Toaster.showToast(errorMsg);
        }
    }

    @Override
    protected void showToast(boolean success, int code, String message) {
        if (code == 204 ||
                code == 2041 ||
                code == 2042 ||
                code == 2043 ||
                code == 3000) {
            //没有数据,不应该算error
            return;
        } else if (code >= 4008 && code <= 4015) {
            //外部处理提示
            return;
        } else if (code == 3001) {
            //登陆异常已经提示过了
            return;
        }
        if (code == kErrorTypeNoNetworkConnect) {
            if (!GlobalUtil.isApplicationInBackground()) {
                Toaster.showToast(R.string.check_network);
            }
        } else {
            if (AppUrlConfig.isDevMode()) {
                Toaster.showToast("下载" + message);
            } else if (message != null || !message.equals("")) {
                Toaster.showToast(message);
            }
        }
    }

    @Override
    protected String getFailToast() {
        return GlobalUtil.getString(R.string.request_fail);
    }

    @Override
    protected void setLoadingViewShow(Boolean show) {
        String content = getLoadingViewContent();
        if (content != null) {
            LoadingUtil.setLoadingViewShowWithContent(show, content);
        } else {
            LoadingUtil.setLoadingViewShow(show);
        }
    }

    /**
     * 定制loadingView的内容
     */
    protected String getLoadingViewContent() {
        return null;
    }
}

