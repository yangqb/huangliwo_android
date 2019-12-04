package com.feitianzhu.fu700.shop.ui;

import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 物流详情
 * */
public class LogisticsInfoActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logistics_info;
    }

    @Override
    protected void initView() {
        titleName.setText("物流信息");
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}
