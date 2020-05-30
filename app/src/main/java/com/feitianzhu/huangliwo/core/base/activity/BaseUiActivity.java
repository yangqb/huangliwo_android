package com.feitianzhu.huangliwo.core.base.activity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.AbsActivity;
import com.feitianzhu.huangliwo.databinding.ActivityBaseUiBinding;
import com.feitianzhu.huangliwo.utils.StringUtils;

/**
 * Created by bch on 2020/5/30.
 *
 */
public abstract class BaseUiActivity extends AbsActivity {
    /**
     * 代替Context
     */
    protected Context mContext;
    private ActivityBaseUiBinding viewDataBinding;

    @Override
    public void initBase() {
        super.initBase();
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_ui);
        viewDataBinding.setViewmodel(this);
        viewDataBinding.llContent.addView(initContentView(getLayoutInflater(), viewDataBinding.llContent));
        mContext = BaseUiActivity.this;
        init();
    }

    public void setBackground(@ColorRes int color) {
        viewDataBinding.titleBackground.setBackgroundResource(color);
    }


    public void onClickBack(View v) {
        finish();
    }

    //容器
    protected abstract View initContentView(LayoutInflater layoutInflater, ViewGroup viewGroup);


    public abstract void init();

    /**
     * 创建一个titlebar
     */
    protected void initTitle(String title) {
        if (!StringUtils.isEmpty(title)) {
            viewDataBinding.titleName.setText(title);
        } else {
            viewDataBinding.titleName.setText("");
        }
    }


}
