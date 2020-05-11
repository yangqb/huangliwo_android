package com.feitianzhu.huangliwo.core.network;

/**
 * Created by gundam on 2017/10/11.
 */

public abstract class BaseApiUpLoadRequest extends BaseApiRequest {

//    public static final String TAG = "BaseApiUpLoadRequest";
//
//    private Subscription sub;
//
//    public Subscription call(final ApiCallBackUpload callback) {
//        //检查网络
//        if (!NetworkStatusUtil.isNetworkConnected(CoreUtils
//                .getApplicationContext())) {
//            callback.onAPIError(0, CoreUtils.getApplicationContext().getResources().getString(R.string.msg_on_no_network));
//            return null;
//        }
//
//
//        sub = Observable.just(needToken())
//                .observeOn(Schedulers.io())//上传文件大 签名速度慢 签名步骤放到io线程
//                .map(sign -> {
//                    printPidId("sign");
//                    return SignatureUtil.getHttpParams(appendParams());
//                })
//                .map(params -> {
//                    printPidId("initReq");
//                    return OkGo.<String>post(getApiUrl())//
//                            .params(SignatureUtil.getHttpParams(appendParams()))//
//                            .converter(new StringConvert());
//                })
//                .map(postRequest -> {
//                    printPidId("initTask");
//                    return OkUpload.request("UpLoadTask", postRequest)//
//                            .priority(1)//.extra1(imageItem)//
//                            .save();
//                })
//                .doOnNext(task -> {
//                    if (callback != null) {
//                        task.register(new LogUploadListener<String>(callback));
//                    }
//                })
//                .subscribe(task -> {
//                            printPidId("reqTask");
//                            LogUtil.d(TAG, "task->start");
//                            task.start();
//                        },
//                        error -> {
//                            callback.onAPIError(0, "网络异常，稍后重试");
//                            LogUtil.d(TAG, error.getMessage());
//                        }//处理异常
//                );
//
//        return sub;
////        PostRequest<String> postRequest = OkGo.<String>post(getApiUrl())//
////                .params(SignatureUtil.getHttpParams(appendParams(), needToken()))//
////                .converter(new StringConvert());
////
////        UploadTask<String> task = OkUpload.request("UpLoadTask", postRequest)//
////                .priority(1)//.extra1(imageItem)//
////                .save();
////
////        if (callback != null) {
////            task.register(new LogUploadListener<String>(callback));
////        }
////        task.start();
//
//    }
//
//
//    public class LogUploadListener<T> extends UploadListener<T> {
//
//        ApiCallBackUpload callback;
//
//        public LogUploadListener() {
//            super("LogUploadListener");
//        }
//
//        public LogUploadListener(ApiCallBackUpload callback) {
//            super("LogUploadListener");
//            this.callback = callback;
//        }
//
//        @Override
//        public void onStart(Progress progress) {
//            LogUtil.d(TAG, "onStart: " + progress);
//        }
//
//        @Override
//        public void onProgress(Progress progress) {
//            LogUtil.d(TAG, "onProgress: " + progress);
//            callback.onProgress(progress);
//            printPidId("callback->onProgress");
//        }
//
//        @Override
//        public void onError(Progress progress) {
//            LogUtil.d(TAG, "onError: " + progress);
//            progress.exception.printStackTrace();
//            callback.onAPIError(0, "网络异常，稍后重试");
//        }
//
//        @Override
//        public void onFinish(T t, Progress progress) {
//            LogUtil.d(TAG, "onFinish: " + progress);
//            Observable.just((String) t).observeOn(Schedulers.io())                 //io线程
//                    .map(resJson -> praseJson(resJson))         //解析json
//                    .map(baseRsq -> praseBaseRsq(baseRsq))      //解析base对象（errorcode extra 字段）
//                    .observeOn(AndroidSchedulers.mainThread())  //切换到主线程
//                    .subscribe(res -> {
//                                handleRsponse_private(callback, res);
//                            },//通知callback返回对象
//                            error -> {
//                                handleError(callback, error);
//                            }//处理异常
//                    );
//        }
//
//        @Override
//        public void onRemove(Progress progress) {
//            LogUtil.d(TAG, "onRemove: " + progress);
//        }
//    }
}
