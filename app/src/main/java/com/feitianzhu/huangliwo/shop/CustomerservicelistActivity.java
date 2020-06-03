package com.feitianzhu.huangliwo.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.core.network.LoadingUtil;
import com.feitianzhu.huangliwo.im.adapter.ConverServiceAdapter;
import com.feitianzhu.huangliwo.im.bean.ConverzServiceListBean;
import com.feitianzhu.huangliwo.im.request.ConverServiceRequest;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomerservicelistActivity extends BaseActivity {

    @BindView(R.id.left_button)
    RelativeLayout leftButton;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.servicerecy)
    RecyclerView servicerecy;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customerservicelist;
    }

    @Override
    protected void initView() {
        titleName.setText("客服列表");
    }

    @Override
    protected void initData() {
        servicerecy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ConverServiceRequest converServiceRequest = new ConverServiceRequest();
        converServiceRequest.call(new ApiCallBack<List<ConverzServiceListBean>>() {
            @Override
            public void onAPIResponse(List<ConverzServiceListBean> response) {
                ConverServiceAdapter adapter = new ConverServiceAdapter(response);
                for (int i = response.size() - 1; i >= 0; i--) {
                    ConverzServiceListBean listBean = response.get(i);
                    if (listBean.getType()!=2){
                        response.remove(i);
                    }
                }
                servicerecy.setAdapter(adapter);
                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        EMClient.getInstance().login("14701776629", "123456", new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                //startActivity(new Intent(Customerservice.this,ImActivity.class));
                                Intent intent = new Intent(CustomerservicelistActivity.this, ImActivity.class);
                                //username为对方的环信id
//                                intent.putExtra(EaseConstant.EXTRA_USER_ID, response.get(position).getUserId());
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
                });
            }

            @Override
            public void onAPIError(int errorCode, String errorMsg) {

            }
        });
    }

    @OnClick(R.id.left_button)
    public void onViewClicked() {
        finish();
    }
}
