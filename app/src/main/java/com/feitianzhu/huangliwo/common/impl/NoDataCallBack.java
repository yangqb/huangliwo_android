package com.feitianzhu.huangliwo.common.impl;

import com.socks.library.KLog;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

import static com.feitianzhu.huangliwo.common.Constant.FailCode;
import static com.feitianzhu.huangliwo.common.Constant.SuccessCode;

/**
 * Created by dicallc on 2017/9/8 0008.
 */

public class NoDataCallBack extends Callback {

    private onConnectionFinishLinstener mLinstener;

    public NoDataCallBack(onConnectionFinishLinstener mLinstener) {
        this.mLinstener = mLinstener;
    }

    @Override
    public Object parseNetworkResponse(String mData, Response response, int id)
            throws Exception {
        return mData;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        mLinstener.onFail(FailCode, e.getMessage());
    }

    @Override
    public void onResponse(Object response, int id) {
        KLog.e(response.toString());
        mLinstener.onSuccess(SuccessCode, "成功");
    }
}
