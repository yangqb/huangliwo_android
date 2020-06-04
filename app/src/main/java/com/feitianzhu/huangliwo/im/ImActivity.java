package com.feitianzhu.huangliwo.im;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import com.feitianzhu.huangliwo.im.runtimepermissions.PermissionsManager;
import com.feitianzhu.huangliwo.im.runtimepermissions.PermissionsResultAction;
import com.feitianzhu.huangliwo.login.LoginActivity;
import com.feitianzhu.huangliwo.me.ui.ScannerActivity;
import com.feitianzhu.huangliwo.model.MineInfoModel;
import com.feitianzhu.huangliwo.utils.SPUtils;
import com.feitianzhu.huangliwo.utils.UserInfoUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

import java.util.List;

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
    public void requestPermission() {
        XXPermissions.with(ImActivity.this)
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                //.constantRequest()
                // 支持请求6.0悬浮窗权限8.0请求安装权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                // 不指定权限则自动获取清单中的危险权限
                .permission(Permission.CAMERA)
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                        } else {
                            ToastUtils.show("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(mContext);
                        } else {
                            ToastUtils.show("获取权限失败");
                        }
                    }
                });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_im;
    }

    @Override
    protected void initView() {
        requestPermission();
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
