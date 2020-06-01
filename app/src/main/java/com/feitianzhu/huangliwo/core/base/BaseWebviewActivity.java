package com.feitianzhu.huangliwo.core.base;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.core.base.activity.BaseBindingActivity;
import com.feitianzhu.huangliwo.core.SkipToUtil;
import com.feitianzhu.huangliwo.core.base.bena.BaseWebviewModel;
import com.feitianzhu.huangliwo.databinding.ActivityBaseWebviewBinding;
import com.feitianzhu.huangliwo.shop.ShopMerchantsDetailActivity;
import com.feitianzhu.huangliwo.shop.ShopsDetailActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;

import java.util.HashMap;
import java.util.Set;


public class BaseWebviewActivity extends BaseBindingActivity {

    private ActivityBaseWebviewBinding dataBinding;
    private static final String kBaseUrlKey = "WebFrag_BaseUrl";
    protected String url = "";

    public static void toBaseWebviewActivity(Context context, String uri) {
        Intent intent = new Intent(context, BaseWebviewActivity.class);
        intent.putExtra(kBaseUrlKey, uri);
        context.startActivity(intent);
    }

    @Override
    public void bindingView() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_webview);
        dataBinding.setViewModel(this);
        WebView webview = dataBinding.webView;

        webview.onResume();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDatabasePath(this.getApplicationContext().getCacheDir().getAbsolutePath());
        String stringExtra = getIntent().getStringExtra(kBaseUrlKey);
        if (StringUtils.isEmpty(stringExtra)) {
            ToastUtils.show("网址为空");
//            finish();
            return;
        }
        url = stringExtra;
        url = "nav=1";
        Log.e("TAG", "bindingView: " + url);
        if (url.contains("nav=1")) {
            dataBinding.headers.setVisibility(View.GONE);
            dataBinding.leftButton1.setVisibility(View.VISIBLE);
        } else {
            dataBinding.headers.setVisibility(View.VISIBLE);
            dataBinding.leftButton1.setVisibility(View.GONE);
        }
        syncCookie(url);
        webview.loadUrl(url);
        webview.addJavascriptInterface(this, "jsBridge");

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("TAG", "onPageStarted: ");

            }
        });

        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                //TODO 更新弹窗
//                AppUpdateManager.update(s);
                Log.e("TAG", "onDownloadStart: ");
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                dataBinding.titleName.setText(title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                if (BaseWebviewActivity.this == null) {
                    return true;
                }
                AlertDialog.Builder b = new AlertDialog.Builder(BaseWebviewActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }
        });
    }

    /**
     * 同步cookie
     *
     * @param url 要加载的地址链接
     */
    private void syncCookie(String url) {

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
//        HashMap<String, String> stringStringHashMap = new HashMap<>();
//        stringStringHashMap.put("bldbyapp", "1");
//        stringStringHashMap.put("token", SPUtils.getString(getApplication(), Constant.SP_ACCESS_TOKEN));
//        stringStringHashMap.put("userId", SPUtils.getString(getApplication(), Constant.SP_LOGIN_USERID));
//        stringStringHashMap.toString()
//        //设置cookie
        cookieManager.setCookie(url, "bldbyapp=1");
        cookieManager.setCookie(url, "token=" + SPUtils.getString(getApplication(), Constant.SP_ACCESS_TOKEN));
        cookieManager.setCookie(url, "userId=" + SPUtils.getString(getApplication(), Constant.SP_LOGIN_USERID));

        //获取Cookie
        String mCookie = cookieManager.getCookie(url);

        //sync
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void init() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dataBinding.webView.reload();
    }


    @JavascriptInterface
    public String openURL(String command) {
        Gson gson = new Gson();
        BaseWebviewModel baseWebviewModel = gson.fromJson(command, BaseWebviewModel.class);
        if (baseWebviewModel.url.startsWith("https://") || baseWebviewModel.url.startsWith("http://")) {
//外部浏览器
            SkipToUtil.toBrowser(this, baseWebviewModel.url);
        } else {
            //    bly://
            Intent intent;
            switch (baseWebviewModel.url) {
                case "goodsDetail":
                    //商品详情
                    intent = new Intent(this, ShopsDetailActivity.class);
                    intent.putExtra(ShopsDetailActivity.GOODS_DETAIL_DATA, baseWebviewModel.param.get("id"));
                    startActivity(intent);
                    break;
                case "storeDetail":
                    //店铺详情
                    intent = new Intent(this, ShopMerchantsDetailActivity.class);
                    intent.putExtra(ShopMerchantsDetailActivity.MERCHANTS_ID, baseWebviewModel.param.get("id"));
                    startActivity(intent);
                    break;
                case "web":
                    //去其他webview
                    toBaseWebviewActivity(this, baseWebviewModel.param.get("url"));
                    break;
                case "back":
                    finish();
                    break;
                default:
                    ToastUtils.show("意外情况,请联系客服");
                    break;
            }
        }
        return "";
    }

}
