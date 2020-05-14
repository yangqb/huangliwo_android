package com.feitianzhu.huangliwo.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by bch on 2020/5/14.
 */

public class ImageCancheUtil {
    /**
     * 获取图片路径
     *
     * @param url
     * @return
     */
    public static String getFilePath(String url) {
        return getImageCachePath() + "/" + url;
    }

    /**
     * 创建splash文件夹
     *
     * @return
     */
    private static String getImageCachePath() {
        File root = new File(Environment.getExternalStorageDirectory(), "/splash/");
        if (!root.exists()) root.mkdirs();
        return root.getPath();
    }

    /**
     * 图片是否已缓存
     */
    private static boolean hasImageCached(String imageUrl) {
        File file = new File(getFilePath(imageUrl));
        return file.exists();
    }

    /**
     * 缓存图片
     *
     * @param activity
     * @param imageUrl    图片网址
     * @param apiCallBack 回调
     */
    private static void cacheImage(Activity activity, String imageUrl, ApiCallBack<String> apiCallBack) {
        Glide.with(activity)
                .asBitmap()
                .load("")
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveBitmap(activity, resource, imageUrl, new ApiCallBack<String>() {
                            @Override
                            public void onAPIResponse(String response) {

                            }

                            @Override
                            public void onAPIError(int errorCode, String errorMsg) {

                            }
                        });
                    }
                });
    }

    private static void saveBitmap(Activity activity, Bitmap bitmap, String filename, ApiCallBack<String> apiCallBack) {
        Observable.just(1)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        String filePath = getFilePath(filename);
                        File file = new File(filePath);
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
                            out.flush();
                            out.close();
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri uri = Uri.fromFile(file);
                            intent.setData(uri);
                            activity.sendBroadcast(intent);//这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file
                            return filePath;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  //切换到主线程
                .subscribe(new Subscriber<String>() {
                               @Override
                               public void onCompleted() {
                               }

                               @Override
                               public void onError(Throwable e) {
                                   apiCallBack.onAPIError(0, "");
                               }

                               @Override
                               public void onNext(String o) {
                                   if (o == null) {
                                       apiCallBack.onAPIError(0, "");
                                   } else {
                                       apiCallBack.onAPIResponse(o);
                                   }
                               }
                           }

                );
    }


}
