package com.feitianzhu.huangliwo.core.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.feitianzhu.huangliwo.common.base.activity.AbsActivity;

/**
 * Created  by bch on 2020/5/6.
 *
 */

public abstract class BaseBindingActivity extends AbsActivity {

    @Override
    public void initBase() {
        super.initBase();
        bindingView();
        init();
    }

    public void onClickBack(View v) {
        finish();
    }

    public abstract void bindingView();

    public abstract void init();

}
