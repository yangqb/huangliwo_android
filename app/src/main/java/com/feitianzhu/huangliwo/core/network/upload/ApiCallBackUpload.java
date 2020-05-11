package com.feitianzhu.huangliwo.core.network.upload;

import com.gowithmi.mapworld.core.network.ApiCallBack;
import com.lzy.okgo.model.Progress;

public interface ApiCallBackUpload extends ApiCallBack {

    public abstract void onProgress(Progress progress);

}
