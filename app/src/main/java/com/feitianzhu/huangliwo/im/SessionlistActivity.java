package com.feitianzhu.huangliwo.im;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.RxCodeConstants;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.core.network.LoadingUtil;
import com.feitianzhu.huangliwo.core.rxbus.RxBus;
import com.feitianzhu.huangliwo.home.HomeFragment;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class SessionlistActivity extends BaseActivity {
    public static boolean s = false;

    private ConversationListFragment conversationListFragment;
    private FragmentTransaction mTransaction;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sessionlist;
    }

    @Override
    protected void initView() {
        mTransaction = getSupportFragmentManager().beginTransaction();
        conversationListFragment = new ConversationListFragment();
        conversationListFragment.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTransaction.add(R.id.fragment_container, conversationListFragment);
        mTransaction.commit();
    }

    @Override
    protected void initData() {

    }
}
