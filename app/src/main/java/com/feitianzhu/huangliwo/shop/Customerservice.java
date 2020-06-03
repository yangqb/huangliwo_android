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
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.core.network.LoadingUtil;
import com.feitianzhu.huangliwo.im.adapter.ConverServiceAdapter;
import com.feitianzhu.huangliwo.im.bean.ConverzServiceListBean;
import com.feitianzhu.huangliwo.im.request.ConverServiceRequest;
import com.gyf.immersionbar.ImmersionBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

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
    private List<ConverzServiceListBean> response;

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
        ConverServiceRequest converServiceRequest = new ConverServiceRequest();
        converServiceRequest.isShowLoading = true;
        converServiceRequest.call(new ApiCallBack<List<ConverzServiceListBean>>() {
            @Override
            public void onAPIResponse(List<ConverzServiceListBean> response) {
                Customerservice.this.response = response;
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {

            }
        });
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
                LoadingUtil.setLoadingViewShow(false);
                if (response != null && response.size() >= 0) {
                    EMClient.getInstance().login("14701776629", "123456", new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().groupManager().loadAllGroups();
                            EMClient.getInstance().chatManager().loadAllConversations();
                            //startActivity(new Intent(Customerservice.this,ImActivity.class));
                            Intent intent = new Intent(Customerservice.this, ImActivity.class);
                            //username为对方的环信id
//                            intent.putExtra(EaseConstant.EXTRA_USER_ID, response.get(0).getUserId());
                            intent.putExtra(EaseConstant.EXTRA_USER_ID, "13671192850");
                            startActivity(intent);
                            Log.d("main", "登录聊天服务器成功!");
                            LoadingUtil.setLoadingViewShow(false);
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }

                        @Override
                        public void onError(int code, String message) {
                            LoadingUtil.setLoadingViewShow(false);
                            Log.i("onError", "onError: " + code + message);
                            Log.d("main", "登录聊天服务器失败！");
                        }
                    });
                }

                break;
            case R.id.returnissue:
                startActivity(new Intent(Customerservice.this, CustomerservicelistActivity.class));
                break;
            case R.id.platformissues:
                startActivity(new Intent(Customerservice.this, CustomerservicelistActivity.class));
                break;
        }
    }
}
