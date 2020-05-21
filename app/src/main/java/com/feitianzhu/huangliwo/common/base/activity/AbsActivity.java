package com.feitianzhu.huangliwo.common.base.activity;

import android.content.Context;
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
import com.umeng.message.PushAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jiangdikai on 2017/9/4.
 * <p>
 * updata by bch on 2020/5/6.
 * 添加ButterKnife
 * 添加状态栏适配
 */

public abstract class AbsActivity extends AppCompatActivity {
    private MaterialDialog mDialog;
    LoadingPopupView loadingPopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();

        if (getOpenImmersionBar() != null) {
            getOpenImmersionBar().init();

        } else {
            ImmersionBar.with(this)
                    .fitsSystemWindows(true)
                    .statusBarDarkFont(true, 0.2f)
                    .navigationBarColor(R.color.white)
                    .statusBarColor(R.color.transparent)
                    .init();
        }
    }

    public ImmersionBar getOpenImmersionBar() {
        return null;
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

//    protected void showloadDialogText(String title) {
//        mDialog = new MaterialDialog.Builder(this)
//                .content(title)
//                .progress(true, 0)
//                .progressIndeterminateStyle(false)
//                .show();
//    }


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
