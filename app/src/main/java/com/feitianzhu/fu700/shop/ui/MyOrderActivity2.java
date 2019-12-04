package com.feitianzhu.fu700.shop.ui;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.badgeview.QBadgeView;

public class MyOrderActivity2 extends BaseActivity {
    @BindView(R.id.all_order)
    TextView allOrder;
    @BindView(R.id.wait_pay_order)
    TextView waitPayOrder;
    @BindView(R.id.wait_sending_order)
    TextView waitSendingOrder;
    @BindView(R.id.wait_receive_order)
    TextView waitReceiveOrder;
    @BindView(R.id.wait_evaluate_order)
    TextView waitEvaluateOrder;
    @BindView(R.id.all_after_sales)
    TextView allAfterSales;
    @BindView(R.id.ll_all_order)
    LinearLayout ll_all_order;
    @BindView(R.id.ll_wait_pay_order)
    LinearLayout ll_wait_pay_order;
    @BindView(R.id.ll_wait_sending_order)
    LinearLayout ll_wait_sending_order;
    @BindView(R.id.ll_wait_receive_order)
    LinearLayout ll_wait_receive_order;
    @BindView(R.id.ll_wait_evaluate_order)
    LinearLayout ll_wait_evaluate_order;
    @BindView(R.id.ll_all_after_sales)
    LinearLayout ll_all_after_sales;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView() {
        titleName.setText("我的订单");
        allOrder.setSelected(true);
        waitPayOrder.setSelected(false);
        waitSendingOrder.setSelected(false);
        waitReceiveOrder.setSelected(false);
        waitEvaluateOrder.setSelected(false);
        allAfterSales.setSelected(false);

        new QBadgeView(this)
                .setGravityOffset(2, 11, true)
                .setBadgeTextColor(getResources().getColor(R.color.color_333333))
                .setBadgeTextSize(11, true)
                .setBadgeBackgroundColor(getResources().getColor(R.color.bg_yellow))
                .bindTarget(ll_all_order)
                .setBadgeNumber(99);

        new QBadgeView(this)
                .setGravityOffset(2, 11, true)
                .setBadgeTextColor(getResources().getColor(R.color.color_333333))
                .setBadgeTextSize(11, true)
                .setBadgeBackgroundColor(getResources().getColor(R.color.bg_yellow))
                .bindTarget(ll_wait_pay_order)
                .setBadgeNumber(15);

        new QBadgeView(this)
                .setGravityOffset(2, 11, true)
                .setBadgeTextColor(getResources().getColor(R.color.color_333333))
                .setBadgeTextSize(11, true)
                .setBadgeBackgroundColor(getResources().getColor(R.color.bg_yellow))
                .bindTarget(ll_wait_sending_order)
                .setBadgeNumber(5);

        new QBadgeView(this)
                .setGravityOffset(2, 11, true)
                .setBadgeTextColor(getResources().getColor(R.color.color_333333))
                .setBadgeTextSize(11, true)
                .setBadgeBackgroundColor(getResources().getColor(R.color.bg_yellow))
                .bindTarget(ll_wait_receive_order)
                .setBadgeNumber(99);

        new QBadgeView(this)
                .setGravityOffset(2, 11, true)
                .setBadgeTextColor(getResources().getColor(R.color.color_333333))
                .setBadgeTextSize(11, true)
                .setBadgeBackgroundColor(getResources().getColor(R.color.bg_yellow))
                .bindTarget(ll_wait_evaluate_order)
                .setBadgeNumber(5);
        new QBadgeView(this)
                .setGravityOffset(2, 11, true)
                .setBadgeTextColor(getResources().getColor(R.color.color_333333))
                .setBadgeTextSize(11, true)
                .setBadgeBackgroundColor(getResources().getColor(R.color.bg_yellow))
                .bindTarget(ll_all_after_sales)
                .setBadgeNumber(58);

    }


    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }

    @Override
    protected void initData() {

    }
}
