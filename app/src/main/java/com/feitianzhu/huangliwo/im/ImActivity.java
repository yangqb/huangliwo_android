package com.feitianzhu.huangliwo.im;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.support.annotation.NonNull;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.RxCodeConstants;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.core.rxbus.RxBus;
import com.feitianzhu.huangliwo.im.request.OtherUserInfoRequest;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

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
        //设置本人头像
        EaseChatRow.meIMG = UserInfoUtils.getUserInfo(this).getHeadImg();
        String stringExtra = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        OtherUserInfoRequest otherUserInfoRequest = new OtherUserInfoRequest();
        String[] split = stringExtra.split("-");
        otherUserInfoRequest.userId = split[0];
        otherUserInfoRequest.isShowLoading = true;
        otherUserInfoRequest.call(new ApiCallBack<MineInfoModel>() {
            @Override
            public void onAPIResponse(MineInfoModel response) {
                //设置聊天头像
                EaseChatRow.OtherIMG = response.getHeadImg();
                //所有未读消息数清零
                EMClient.getInstance().chatManager().markAllConversationsAsRead();
                RxBus.getDefault().post(RxCodeConstants.IM_MESSAGE, false);

                chatFragment = new EaseChatFragment();
                chatFragment.setAvatar(UserInfoUtils.getUserInfo(ImActivity.this).getHeadImg());
                chatFragment.setName(UserInfoUtils.getUserInfo(ImActivity.this).getNickName());
                chatFragment.setTitle(response.getNickName());
                //pass parameters to chat fragment
                chatFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {
                ToastUtils.show("客服信息获取失败");
                finish();
//                chatFragment = new EaseChatFragment();
//                chatFragment.setAvatar(UserInfoUtils.getUserInfo(ImActivity.this).getHeadImg());
//                chatFragment.setName(UserInfoUtils.getUserInfo(ImActivity.this).getNickName());
////                chatFragment.setTitle(response.getNickName());
//                //pass parameters to chat fragment
//                chatFragment.setArguments(getIntent().getExtras());
//                getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    public ImmersionBar getOpenImmersionBar() {
        return null;
    }
}
