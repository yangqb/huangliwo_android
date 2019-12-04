package com.feitianzhu.fu700.shop.ui;

import android.view.View;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        titleName.setText("订单详情");
    }

    @OnClick(R.id.left_button)
    public void onClick(View view) {
        finish();
    }

    @Override
    protected void initData() {

    }
}
