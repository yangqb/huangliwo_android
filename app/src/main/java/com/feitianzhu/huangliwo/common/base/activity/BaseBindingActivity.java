package com.feitianzhu.huangliwo.common.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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
