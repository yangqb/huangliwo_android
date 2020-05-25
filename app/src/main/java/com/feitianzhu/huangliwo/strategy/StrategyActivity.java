package com.feitianzhu.huangliwo.strategy;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.common.base.activity.BaseBindingActivity;
import com.feitianzhu.huangliwo.databinding.ActivityStrategyBinding;

public class StrategyActivity extends BaseBindingActivity {

    private ActivityStrategyBinding dataBinding;

    @Override
    public void bindingView() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_strategy);
        dataBinding.setViewmodel(this);
    }

    @Override
    public void init() {

    }
}
