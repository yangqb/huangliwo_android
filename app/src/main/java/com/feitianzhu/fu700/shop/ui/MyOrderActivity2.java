package com.feitianzhu.fu700.shop.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.feitianzhu.fu700.R;
import com.feitianzhu.fu700.me.base.BaseActivity;
import com.feitianzhu.fu700.shop.ShopPayActivity;
import com.feitianzhu.fu700.shop.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyOrderActivity2 extends BaseActivity {
    private OrderAdapter adapter;
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
    @BindView(R.id.line_all)
    View lineAll;
    @BindView(R.id.line_wait_pay)
    View lineWaitPay;
    @BindView(R.id.line_wait_sending)
    View lineWaitSending;
    @BindView(R.id.line_wait_receive)
    View lineWaitReceive;
    @BindView(R.id.line_wait_evaluate)
    View lineWaitEvaluate;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.img_collect)
    ImageView afterSales;
    @BindView(R.id.right_img)
    ImageView search;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView() {
        titleName.setText("我的订单");
        search.setBackgroundResource(R.mipmap.a01_03shouhou);
        afterSales.setBackgroundResource(R.mipmap.a01_03sousuo);
        search.setVisibility(View.VISIBLE);
        afterSales.setVisibility(View.VISIBLE);
        allOrder.setSelected(true);
        waitPayOrder.setSelected(false);
        waitSendingOrder.setSelected(false);
        waitReceiveOrder.setSelected(false);
        waitEvaluateOrder.setSelected(false);
        lineAll.setSelected(true);
        lineWaitPay.setSelected(false);
        lineWaitSending.setSelected(false);
        lineWaitReceive.setSelected(false);
        lineWaitEvaluate.setSelected(false);

        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            integers.add(i);
        }

        adapter = new OrderAdapter(integers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        initListener();

    }

    public void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(MyOrderActivity2.this, OrderDetailActivity.class);
                startActivity(intent);
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //根据订单类型来跳转1.交易成功(已收货)2.等待付款(下单成功)3.等待发货(付款成功)4.已发货5.等待收货6.退款中7.退款成功8.订单失效
                switch (view.getId()) {
                    case R.id.btn_refund: //退款
                        break;
                    case R.id.btn_logistics: //查看物流
                        break;
                    case R.id.btn_confirm_goods: //确认收货，
                        Intent intent;
                        if (position == 0) {
                            //评价
                            intent = new Intent(MyOrderActivity2.this, EditCommentsActivity.class);
                            startActivity(intent);
                        } else if (position == 1) {
                            //付款
                            intent = new Intent(MyOrderActivity2.this, ShopPayActivity.class);
                            startActivity(intent);
                        } else if (position == 2) {
                            //查看物流(已发货)
                            intent = new Intent(MyOrderActivity2.this, LogisticsInfoActivity.class);
                            startActivity(intent);
                        } else if (position == 3) {
                            //申请退款
                            intent = new Intent(MyOrderActivity2.this, EditApplyRefundActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }


    @OnClick({R.id.left_button, R.id.all_order, R.id.wait_pay_order, R.id.wait_sending_order, R.id.wait_receive_order, R.id.wait_evaluate_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button:
                finish();
                break;
            case R.id.all_order:
                setSelect(allOrder, true);
                setSelect(waitPayOrder, false);
                setSelect(waitSendingOrder, false);
                setSelect(waitReceiveOrder, false);
                setSelect(waitEvaluateOrder, false);
                setSelect(lineAll, true);
                setSelect(lineWaitPay, false);
                setSelect(lineWaitSending, false);
                setSelect(lineWaitReceive, false);
                setSelect(lineWaitEvaluate, false);
                break;
            case R.id.wait_pay_order:
                setSelect(allOrder, false);
                setSelect(waitPayOrder, true);
                setSelect(waitSendingOrder, false);
                setSelect(waitReceiveOrder, false);
                setSelect(waitEvaluateOrder, false);
                setSelect(lineAll, false);
                setSelect(lineWaitPay, true);
                setSelect(lineWaitSending, false);
                setSelect(lineWaitReceive, false);
                setSelect(lineWaitEvaluate, false);
                break;
            case R.id.wait_sending_order:
                setSelect(allOrder, false);
                setSelect(waitPayOrder, false);
                setSelect(waitSendingOrder, true);
                setSelect(waitReceiveOrder, false);
                setSelect(waitEvaluateOrder, false);
                setSelect(lineAll, false);
                setSelect(lineWaitPay, false);
                setSelect(lineWaitSending, true);
                setSelect(lineWaitReceive, false);
                setSelect(lineWaitEvaluate, false);
                break;
            case R.id.wait_receive_order:
                setSelect(allOrder, false);
                setSelect(waitPayOrder, false);
                setSelect(waitSendingOrder, false);
                setSelect(waitReceiveOrder, true);
                setSelect(waitEvaluateOrder, false);
                setSelect(lineAll, false);
                setSelect(lineWaitPay, false);
                setSelect(lineWaitSending, false);
                setSelect(lineWaitReceive, true);
                setSelect(lineWaitEvaluate, false);
                break;
            case R.id.wait_evaluate_order:
                setSelect(allOrder, false);
                setSelect(waitPayOrder, false);
                setSelect(waitSendingOrder, false);
                setSelect(waitReceiveOrder, true);
                setSelect(waitEvaluateOrder, false);
                setSelect(lineAll, false);
                setSelect(lineWaitPay, false);
                setSelect(lineWaitSending, false);
                setSelect(lineWaitReceive, false);
                setSelect(lineWaitEvaluate, true);
                break;
        }

    }

    public void setSelect(View view, boolean select) {
        view.setSelected(select);
    }

    @Override
    protected void initData() {

    }
}
