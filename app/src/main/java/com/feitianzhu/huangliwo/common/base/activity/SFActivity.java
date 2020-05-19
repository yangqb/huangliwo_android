package com.feitianzhu.huangliwo.common.base.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feitianzhu.huangliwo.GlobalUtil;
import com.feitianzhu.huangliwo.R;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jiangdikai on 2017/9/4.
 * <p>
 * updata by bch on 2020/5/6.
 * 添加ButterKnife
 * 添加状态栏适配
 */

public abstract class SFActivity extends AppCompatActivity {
    private MaterialDialog mDialog;
    protected Context sfContext;
    private Unbinder mBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        if (getOpenImmersionBar()) {
            ImmersionBar.with(this)
                    .fitsSystemWindows(true)
                    .navigationBarColor(R.color.white)
                    .navigationBarDarkIcon(true)
                    .statusBarDarkFont(true, 0.2f)
                    .statusBarColor(R.color.white)
                    .init();
        }

        mBinder = ButterKnife.bind(this);


        sfContext = this;
    }

    public boolean getOpenImmersionBar() {
        return true;
    }

    /**
     * 子类传入一个布局,父类创建View
     */
    protected abstract int getLayoutId();

    @Override
    protected void onResume() {
        super.onResume();
        GlobalUtil.setCurrentActivity(this);

        onBaseResume();
    }

    protected void onBaseResume() {

    }

    protected void showloadDialog(String title) {
        mDialog = new MaterialDialog.Builder(this)
                .content("加载中,请稍等")
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
    }

    protected void showloadDialogText(String title) {
        mDialog = new MaterialDialog.Builder(this)
                .content(title)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show();
    }

    protected void goneloadDialog() {
        if (null != mDialog && mDialog.isShowing()) mDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        if (mBinder != null) {
            mBinder.unbind();
        }
        super.onDestroy();
        mDialog = null;
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
