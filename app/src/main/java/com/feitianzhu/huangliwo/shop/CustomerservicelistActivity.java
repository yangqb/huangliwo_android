package com.feitianzhu.huangliwo.shop;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.base.activity.BaseActivity;
import com.feitianzhu.huangliwo.core.network.ApiCallBack;
import com.feitianzhu.huangliwo.im.adapter.ConverServiceAdapter;
import com.feitianzhu.huangliwo.im.bean.ConverzServiceListBean;
import com.feitianzhu.huangliwo.im.request.ConverServiceRequest;

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
        servicerecy.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ConverServiceRequest converServiceRequest = new ConverServiceRequest();
        converServiceRequest.call(new ApiCallBack<List<ConverzServiceListBean>>() {
            @Override
            public void onAPIResponse(List<ConverzServiceListBean> response) {
                   ConverServiceAdapter adapter=new ConverServiceAdapter(response);
                    servicerecy.setAdapter(adapter);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
