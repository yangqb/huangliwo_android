package com.feitianzhu.fu700.shop.ui;

import android.view.View;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class EditApplyRefundActivity extends BaseActivity {

    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.right_text)
    TextView rightText;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_refund;
    }

    @Override
    protected void initView() {
        titleName.setText("申请退款");
        rightText.setText("提交");
        rightText.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }


    @Override
    protected void initData() {

    }
}
