package com.feitianzhu.huangliwo.common.base.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feitianzhu.huangliwo.GlobalUtil;
import com.feitianzhu.huangliwo.R;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.Unbinder;

/**
 * Created by jiangdikai on 2017/9/4.
 * <p>
 * updata by bch on 2020/5/6.
 * 添加ButterKnife
 * 添加状态栏适配
 */

public abstract class BaseBindingActivity extends AbsActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingView();
        init();
    }


    public void onClickBack(View v) {
        finish();
    }

    public abstract void bindingView();

    public abstract void init();

}
