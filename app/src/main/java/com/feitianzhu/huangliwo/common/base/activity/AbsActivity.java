package com.feitianzhu.huangliwo.common.base.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feitianzhu.huangliwo.GlobalUtil;
import com.feitianzhu.huangliwo.R;
import com.gyf.immersionbar.ImmersionBar;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.impl.LoadingPopupView;

/**
 * Created by  bch on 2020/5/6.
 */

public abstract class AbsActivity extends AppCompatActivity {
    LoadingPopupView loadingPopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalUtil.setCurrentActivity(this);
        initBase();
//        PushAgent.getInstance(this).onAppStart();

        if (getOpenImmersionBar() != null) {
            getOpenImmersionBar().init();

        }

    }
    public void initBase() {

    }

    public ImmersionBar getOpenImmersionBar() {
        return ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarDarkFont(true, 0.2f)
                .navigationBarColor(R.color.white)
                .statusBarColor(R.color.white)
                .navigationBarDarkIcon(true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        GlobalUtil.setCurrentActivity(this);
    }


    protected void showloadDialog(String title) {
        loadingPopup = (LoadingPopupView) new XPopup.Builder(this)
                .hasShadowBg(false)
                .popupAnimation(PopupAnimation.NoAnimation)
                .asLoading()
                .bindLayout(R.layout.layout_loading_view)
                .show();
    }

    protected void goneloadDialog() {
        if (null != loadingPopup) {
            loadingPopup.delayDismissWith(600, new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingPopup = null;
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration newConfig = new Configuration();
        //控制字体缩放 1.0为默认
        newConfig.fontScale = 1.0f;
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //7.0以上系统手机 显示大小 对APP的影响
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (displayMetrics.density < DisplayMetrics.DENSITY_DEVICE_STABLE / (float) DisplayMetrics.DENSITY_DEFAULT) {
                    displayMetrics.densityDpi = (int) (DisplayMetrics.DENSITY_DEVICE_STABLE * 0.92);
                } else {
                    displayMetrics.densityDpi = DisplayMetrics.DENSITY_DEVICE_STABLE;
                }
                newConfig.densityDpi = displayMetrics.densityDpi;
            }
            createConfigurationContext(newConfig);
        }
        res.updateConfiguration(newConfig, displayMetrics);
        return res;
    }
}
