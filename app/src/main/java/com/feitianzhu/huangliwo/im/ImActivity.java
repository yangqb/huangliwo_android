package com.feitianzhu.huangliwo.im;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.support.annotation.NonNull;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.RxCodeConstants;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.core.rxbus.RxBus;
import com.gyf.immersionbar.ImmersionBar;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;

import static com.hyphenate.easeui.utils.EaseUserUtils.getUserInfo;

public class ImActivity extends BaseActivity {
    //    userId-dev
//    userId-pro
    private EaseChatFragment chatFragment;

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_im;
    }

    @Override
    protected void initView() {
        String name = getIntent().getStringExtra("name");
        String icon = getIntent().getStringExtra("icon");
        //所有未读消息数清零
        EMClient.getInstance().chatManager().markAllConversationsAsRead();
        RxBus.getDefault().post(RxCodeConstants.IM_MESSAGE, false);

        EaseUI easeUI = EaseUI.getInstance();
//需要easeui库显示用户头像和昵称设置此provider
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                EaseUser easeUser = new EaseUser(name);
                easeUser.setAvatar(icon);
                return easeUser;
//                return getUserInfo(username);
            }
        });
        //use EaseChatFratFragment
        chatFragment = new EaseChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    protected void initData() {

    }

    @Override
    public ImmersionBar getOpenImmersionBar() {
        return null;
    }
}
