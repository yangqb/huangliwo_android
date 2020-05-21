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
import com.lxj.xpopup.impl.LoadingPopupView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jiangdikai on 2017/9/4.
 * <p>
 * updata by bch on 2020/5/6.
 * 添加ButterKnife
 * 添加状态栏适配
 */

public abstract class SFActivity extends AbsActivity {
    protected Context sfContext;
    private Unbinder mBinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        mBinder = ButterKnife.bind(this);

        sfContext = this;
    }


    /**
     * 子类传入一个布局,父类创建View
     */
    protected abstract int getLayoutId();

    @Override
    protected void onResume() {
        super.onResume();

        onBaseResume();
    }

    protected void onBaseResume() {

    }

    @Override
    protected void onDestroy() {
        if (mBinder != null) {
            mBinder.unbind();
        }
        super.onDestroy();
    }

}
