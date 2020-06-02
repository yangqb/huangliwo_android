package com.feitianzhu.huangliwo.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.support.annotation.NonNull;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

public class ImActivity extends BaseActivity {

    public static ImActivity activityInstance;
    private EaseChatFragment chatFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_im;
    }

    @Override
    protected void initView() {
        //所有未读消息数清零
        EMClient.getInstance().chatManager().markAllConversationsAsRead();

        EaseUI easeUI = EaseUI.getInstance();
//需要easeui库显示用户头像和昵称设置此provider
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {

            @Override
            public EaseUser getUser(String username) {
                EaseUser easeUser = new EaseUser("heh");
                easeUser.setAvatar("http://8.129.218.83:8088/user/merchant/2020/02/06/b7c4ca93a5f54acaafabe8c74d9da213.jpg");
                return easeUser;
//                return getUserInfo(username);
            }
        });
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
