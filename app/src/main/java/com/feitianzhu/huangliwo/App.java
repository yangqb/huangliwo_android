package com.feitianzhu.huangliwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;

import com.mob.MobApplication;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.tencent.bugly.crashreport.CrashReport;
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

    static {//使用static代码段可以防止内存泄漏
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
                //layout.setReboundDuration(1000);//回弹动画时长（毫秒）
                layout.setDisableContentWhenLoading(false);//是否在加载更多的时候禁止列表的操作
                layout.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
                layout.setEnableLoadMoreWhenContentNotFull(false);//在内容不满一页的时候，是否可以上拉加载更多
                // layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
                layout.setEnableFooterTranslationContent(true);
                return new BallPulseFooter(context).setAnimatingColor(context.getResources().getColor(R.color.bg_yellow)).setNormalColor(context.getResources().getColor(R.color.bg_yellow));
            }
        });

        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
                layout.setEnableHeaderTranslationContent(false);//拖动Header的时候是否同时拖动内容（默认true）
                return new MaterialHeader(context).setColorSchemeColors(context.getResources().getColor(R.color.bg_yellow));
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ZXingLibrary.initDisplayOpinion(this);
        initOkUtils();
        //SDKInitializer.initialize(getApplicationContext());
        initPush();
        AutoSizeConfig.getInstance().setCustomFragment(true);
        CrashReport.initCrashReport(getApplicationContext(), "ad4dea9550", false);
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
