package com.feitianzhu.huangliwo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.feitianzhu.huangliwo.analyze.UMengAnalyze;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.core.log.HttpLogUtil;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetWorkState;
import com.feitianzhu.huangliwo.core.network.networkcheck.NetworkConnectChangedReceiver;
import com.feitianzhu.huangliwo.core.rxbus.RxBus;
import com.feitianzhu.huangliwo.core.util.CrashHandler;
import com.feitianzhu.huangliwo.utils.ToastWhiteStyle2;
import com.feitianzhu.huangliwo.view.MRefreshHeader;
import com.hjq.toast.ToastUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.push.EMPushConfig;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.concurrent.TimeUnit;

import me.jessyan.autosize.AutoSizeConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by dicallc on 2017/9/4 0004.
 */

public class App extends Application {
    static App context;

    static {//使用static代码段可以防止内存泄漏
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        ClassicsFooter.REFRESH_FOOTER_FINISH = "";
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
                //layout.setReboundDuration(1000);//回弹动画时长（毫秒）
                layout.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
                layout.setDisableContentWhenLoading(false);//是否在加载更多的时候禁止列表的操作
                layout.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
                layout.setEnableLoadMoreWhenContentNotFull(false);//在内容不满一页的时候，是否可以上拉加载更多
                layout.setEnableFooterFollowWhenNoMoreData(false);//置是否在没有更多数据之后 Footer 跟随内容
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
                //return new BallPulseFooter(context).setAnimatingColor(context.getResources().getColor(R.color.bg_yellow)).setNormalColor(context.getResources().getColor(R.color.bg_yellow));
                //return new MRefreshFooter(context);
                //return new FalsifyFooter(context);
                return new ClassicsFooter(context);
            }
        });

        //全局设置默认的 Header
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //开始设置全局的基本参数（这里设置的属性只跟下面的MaterialHeader绑定，其他Header不会生效，能覆盖DefaultRefreshInitializer的属性和Xml设置的属性）
                layout.setEnableHeaderTranslationContent(true);//拖动Header的时候是否同时拖动内容（默认true）
                //return new MaterialHeader(context).setColorSchemeColors(context.getResources().getColor(R.color.bg_yellow));
                return new MRefreshHeader(context);
            }
        });


    }

    {

        PlatformConfig.setWeixin(Constant.WX_APP_ID, "9e4574f38b2b81b24f5305105626bd03");
        PlatformConfig.setQQZone("1110519400", "YenznwVDRAFyMEgf");
        PlatformConfig.setQQFileProvider("com.feitianzhu.huangliwo.fileprovider");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
// 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
// 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
//初始化
        EaseUI.getInstance().init(context, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
//EaseUI初始化成功之后再去调用注册消息监听的代码
        ZXingLibrary.initDisplayOpinion(this);
        //vivo推送
        EMPushConfig.Builder builder = new EMPushConfig.Builder(context);
        builder.enableVivoPush();
        options.setPushConfig(builder.build());
        initOkgo();
        initPush();

        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
        EMTextMessageBody txtBody = new EMTextMessageBody("test");
        message.setTo("13671192850");
// 设置自定义扩展字段
        message.setAttribute("em_force_notification", true);
// 设置消息回调
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(context,"成功",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
// 发送消息
        EMClient.getInstance().chatManager().sendMessage(message);


        AutoSizeConfig.getInstance().setCustomFragment(true);//屏幕适配
        SDKInitializer.initialize(this);
        ToastUtils.init(this);
        ToastUtils.initStyle(new ToastWhiteStyle2(this));

        UMengAnalyze.getInstance().openLog(true);

        UMengAnalyze.getInstance().init(context);
        //微信登录是否每次授权============
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        //微信登录是否每次授权===========

        //网络监听日志
        NetworkConnectChangedReceiver.addNetworkConnectChangedListener(context, new NetworkConnectChangedReceiver.NetworkChangedListener() {
            @Override
            public void onNetworkChanged(NetWorkState networkStatus) {
                RxBus.getDefault().post(RxCodeConstants.NETWORKSTATUS, networkStatus);
            }
        });
        GlobalUtil.setApplication(context);

        CrashHandler.getInstance(this).setCallBack(new CrashHandler.CrashHandlerCallBack() {
            @Override
            public void onCatchException(Throwable ex) {

            }
        });
    }


    public void initOkgo() {
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        /*HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1"); //header不支持中文
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1"); //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");*/
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //超时时间设置，默认60秒
        builder.readTimeout(30000, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(30000, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(30000, TimeUnit.MILLISECONDS);   //全局的连接超时时间

        //自动管理cookie（或者叫session的保持），以下几种任选其一就行

        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));      //使用数据库保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                HttpLogUtil.i("HttpLoggingInterceptor", message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (false) {
            builder.addInterceptor(httpLoggingInterceptor);
        }
        /*//https相关设置，以下几种方案根据需要自己设置

        //方法二：自定义信任规则，校验服务端证书
        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(new SafeHostnameVerifier());*/

        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        // 其他统一的配置
        // 详细说明看GitHub文档：https://github.com/jeasonlzy/
        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                /*.addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数*/
    }

    private void initPush() {

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
