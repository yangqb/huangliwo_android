package com.feitianzhu.huangliwo.im;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.home.HomeFragment;
import com.hyphenate.chat.EMClient;

public class SessionlistActivity extends BaseActivity {
       private ConversationListFragment conversationListFragment;
    private FragmentTransaction mTransaction;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sessionlist;
    }

    @Override
    protected void initView() {
        //所有未读消息数清零
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
        mTransaction = getSupportFragmentManager().beginTransaction();
        conversationListFragment = new ConversationListFragment();
        mTransaction.add(R.id.fragment_container, conversationListFragment);
        mTransaction.commit();
    }

    @Override
    protected void initData() {

    }
}
