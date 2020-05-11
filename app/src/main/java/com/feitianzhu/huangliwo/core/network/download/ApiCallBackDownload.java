package com.feitianzhu.huangliwo.core.network.download;

import com.lzy.okgo.model.Progress;

import java.io.File;

public  interface ApiCallBackDownload {

    /**
     * 成功添加任务的回调
     */
    void onStart(Progress progress);

    /**
     * 下载进行时回调
     */
    void onProgress(Progress progress);


    /**
     * 下载完成时回调
     */
    void onFinish(File t, Progress progress);

    /**
     * 被移除时回调
     */
    void onRemove(Progress progress);

    void onAPIError(int errorCode, String errorMsg, Progress progress);
}
