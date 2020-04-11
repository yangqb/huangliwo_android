package com.feitianzhu.huangliwo.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;

/**
 * package name: com.feitianzhu.huangliwo.view
 * user: yangqinbo
 * date: 2020/4/6
 * time: 13:04
 * email: 694125155@qq.com
 */
public class CustomPlaneProtocolDialog extends Dialog {
    private Context context;
    private LinearLayout container;
    private AgentWeb mAgentWeb;
    private String url;
    private String title = "";
    private TextView titleName;
    private TextView btnSubmit;

    public CustomPlaneProtocolDialog(Context context) {
        super(context, R.style.BottomDialog);
        this.context = context;
    }

    public CustomPlaneProtocolDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public CustomPlaneProtocolDialog setData(String url) {
        this.url = url;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_plane_protocol);
        container = findViewById(R.id.container);
        titleName = findViewById(R.id.titleName);
        btnSubmit = findViewById(R.id.btnSubmit);
        if (TextUtils.isEmpty(title) || title == null) {
            titleName.setVisibility(View.GONE);
        } else {
            titleName.setVisibility(View.VISIBLE);
            titleName.setText(title);
        }
        mAgentWeb = AgentWeb.with((Activity) context)//传入Activity or Fragment
                .setAgentWebParent(container, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .useDefaultIndicator()// 使用默认进度条
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
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
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        //设置自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                AgentWebConfig.clearDiskCache(context);
                mAgentWeb.getWebLifeCycle().onDestroy();
            }
        });
    }

    private com.just.agentweb.WebChromeClient chromeClient = new WebChromeClient() {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    };
    private com.just.agentweb.WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    };

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }
}
