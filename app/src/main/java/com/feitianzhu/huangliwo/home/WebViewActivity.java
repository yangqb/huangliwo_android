package com.feitianzhu.huangliwo.home;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Lee on 2017/9/18.
 */

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.wb_show)
    WebView mWbShow;
    private String mUrl;
    private String mTitle;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
//        setTitleName(name);
        WebSettings settings = mWbShow.getSettings();
        //支持屏幕缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        //不显示webview缩放按钮
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        //设置自适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        showloadDialog("加载中");
    }

    private void initWebView(String mShopUrl) {
        mWbShow.loadUrl(mShopUrl);
        mWbShow.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        mWbShow.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    goneloadDialog();
                    // 网页加载完成
                } else {

                }

            }
        });
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void initData() {
        mUrl = getIntent().getStringExtra("url");
        mTitle = getIntent().getStringExtra("title");

        mTitle = TextUtils.isEmpty(mTitle) ? "网页" : mTitle;
        titleName.setText(mTitle);
        initWebView(mUrl);
    }

    public static void startActivity(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
