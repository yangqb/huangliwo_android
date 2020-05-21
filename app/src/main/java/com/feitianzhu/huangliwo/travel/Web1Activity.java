package com.feitianzhu.huangliwo.travel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.SFActivity;
import com.feitianzhu.huangliwo.pushshop.EditMerchantsActivity;
import com.feitianzhu.huangliwo.pushshop.PushShopListActivity;
import com.feitianzhu.huangliwo.travel.bean.OilListBean;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.StringUtils;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.common.base
 * user: yangqinbo
 * date: 2020/4/11
 * time: 14:35
 * email: 694125155@qq.com
 */
public class Web1Activity extends SFActivity {

    @BindView(R.id.container)
    LinearLayout container;
    private AgentWeb mAgentWeb;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.head)
    LinearLayout head;

    public static void toWeb1Activity(AppCompatActivity appCompatActivity, String uri) {
        Intent intent = new Intent(appCompatActivity, Web1Activity.class);
        intent.putExtra(Constant.URL, uri);
        appCompatActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        titleName.setText("支付详情");
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white)
                .init();
        String url = getIntent().getStringExtra(Constant.URL);
        //url = "https://www.baidu.com/";
        String title = getIntent().getStringExtra(Constant.H5_TITLE);
        if (TextUtils.isEmpty(title) || title == null) {
            head.setVisibility(View.GONE);
        } else {
            head.setVisibility(View.VISIBLE);
            titleName.setText(title);
        }
        mAgentWeb = AgentWeb.with(this)//传入Activity or Fragment
                .setAgentWebParent(container, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setWebChromeClient(chromeClient)
                .setWebViewClient(webViewClient)
                .createAgentWeb()//
                .ready()
//                .go("http://www.jd.com");
                .go(url);
        mAgentWeb.getWebCreator().getWebView().setVerticalScrollBarEnabled(false); //垂直不显示
        //支持屏幕缩放
        WebSettings webSettings = mAgentWeb.getAgentWebSettings().getWebSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        //设置自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web1_bottom;
    }

    private WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            //titleName.setText(title);
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    };
    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    };

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        AgentWebConfig.clearDiskCache(this);
        AgentWebConfig.removeAllCookies();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        WebView mWebView = mAgentWeb.getWebCreator().getWebView();
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            // 返回上一页面
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.left_button})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.left_button:
                // 返回上一页面
                WebView mWebView = mAgentWeb.getWebCreator().getWebView();
                if (mWebView.canGoBack()) {
                    mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    mWebView.goBack();
                } else {
                    finish();
                }
                break;
            default:
                break;

        }
    }
}
