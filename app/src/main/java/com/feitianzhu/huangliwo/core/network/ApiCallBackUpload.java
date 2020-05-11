package com.feitianzhu.huangliwo.core.network;

import com.lzy.okgo.model.Progress;

public interface ApiCallBackUpload extends ApiCallBack {

    public abstract void onProgress(Progress progress);

}
