package com.feitianzhu.huangliwo.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.hyphenate.easeui.ui.EaseChatFragment;

public class ImActivity extends BaseActivity {

    public static ImActivity activityInstance;
    private EaseChatFragment chatFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_im;
    }

    @Override
    protected void initView() {
        activityInstance = this;
        //use EaseChatFratFragment
        chatFragment = new EaseChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    protected void initData() {

    }
}
