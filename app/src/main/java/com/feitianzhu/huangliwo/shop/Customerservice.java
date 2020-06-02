package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.OnClick;

public class Customerservice extends BaseActivity {

    @BindView(R.id.left_button)
    RelativeLayout leftButton;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.shopissues)
    LinearLayout shopissues;
    @BindView(R.id.returnissue)
    LinearLayout returnissue;
    @BindView(R.id.platformissues)
    LinearLayout platformissues;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_customerservice;
    }

    @Override
    protected void initView() {
        titleName.setText("在线客服");
    }

    @Override
    protected void initData() {

    }

    @Override
    public ImmersionBar getOpenImmersionBar() {
        return ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .navigationBarColor(R.color.white)
                .navigationBarDarkIcon(true)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.white);
    }

    @OnClick({R.id.left_button, R.id.shopissues, R.id.returnissue, R.id.platformissues})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.shopissues:
                EMClient.getInstance().login("13671192850","123456",new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        //startActivity(new Intent(Customerservice.this,ImActivity.class));
                        Intent intent = new Intent(Customerservice.this, ImActivity.class);
                       //username为对方的环信id
                        intent.putExtra(EaseConstant.EXTRA_USER_ID, "14701776629");
                        startActivity(intent);
                        Log.d("main", "登录聊天服务器成功！");
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.i("onError", "onError: "+code+message);
                        Log.d("main", "登录聊天服务器失败！");
                    }
                });
                break;
            case R.id.returnissue:
                 startActivity(new Intent(Customerservice.this,CustomerservicelistActivity.class));
                break;
            case R.id.platformissues:
                startActivity(new Intent(Customerservice.this,CustomerservicelistActivity.class));
                break;
        }
    }
}
