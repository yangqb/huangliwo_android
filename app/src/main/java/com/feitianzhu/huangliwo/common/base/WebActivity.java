package com.feitianzhu.huangliwo.common.base;

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
import com.feitianzhu.huangliwo.pushshop.EditMerchantsActivity;
import com.feitianzhu.huangliwo.pushshop.PushShopListActivity;
import com.feitianzhu.huangliwo.pushshop.PushShopProtocolActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
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
public class WebActivity extends AppCompatActivity {
    public static final String PUSH_PROTOCOL = "push_protocol";
    private boolean isPushProtocol;
    @BindView(R.id.container)
    LinearLayout container;
    private AgentWeb mAgentWeb;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.head)
    LinearLayout head;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_bottom);
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white)
                .init();
        isPushProtocol = getIntent().getBooleanExtra(PUSH_PROTOCOL, false);
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

    @OnClick({R.id.left_button, R.id.not_agreed, R.id.agreed})
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
            case R.id.not_agreed:
                if (isPushProtocol) {
                    SPUtils.putBoolean(this, Constant.SP_PUSH_SHOP_PROTOCOL, false);
                } else {
                    SPUtils.putBoolean(this, Constant.SP_PUSH_SHOP_INSTRUCTIONS, false);
                }
                finish();
                break;
            case R.id.agreed:
                if (isPushProtocol) {
                    intent = new Intent(WebActivity.this, PushShopListActivity.class);
                    startActivity(intent);
                    SPUtils.putBoolean(this, Constant.SP_PUSH_SHOP_PROTOCOL, true);
                    finish();
                } else {
                    intent = new Intent(WebActivity.this, EditMerchantsActivity.class);
                    startActivity(intent);
                    SPUtils.putBoolean(this, Constant.SP_PUSH_SHOP_INSTRUCTIONS, true);
                    finish();
                }
                break;
        }
    }
}
