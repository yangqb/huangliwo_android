package com.feitianzhu.fu700;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.mob.MobApplication;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import me.jessyan.autosize.AutoSizeConfig;
import okhttp3.OkHttpClient;

/**
 * Created by dicallc on 2017/9/4 0004.
 */

public class App extends MobApplication {
    static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ZXingLibrary.initDisplayOpinion(this);
        initOkUtils();
        //SDKInitializer.initialize(getApplicationContext());
        initPush();
        AutoSizeConfig.getInstance().setCustomFragment(true);
    }

    private void initPush() {

    }


    private void initOkUtils() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor("feitianzhu"))
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static Context getAppContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
