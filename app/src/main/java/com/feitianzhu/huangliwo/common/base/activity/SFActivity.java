package com.feitianzhu.huangliwo.common.base.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

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
            mBinder = ButterKnife.bind(this);
        }

        sfContext = this;
        initBind();
    }

    protected void initBind() {

    }

    /**
     * 子类传入一个布局,父类创建View
     */
    @LayoutRes
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
