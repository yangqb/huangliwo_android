package com.feitianzhu.huangliwo.strategy;

import android.databinding.DataBindingUtil;

import com.feitianzhu.huangliwo.R;
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
